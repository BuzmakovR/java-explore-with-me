package ru.practicum.explorewithme.service.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.client.StatsClient;
import ru.practicum.explorewithme.dto.ViewStatsDto;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.dto.event.UpdateEventAdminRequest;
import ru.practicum.explorewithme.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.dto.event.fitler.EventAdminSearchFilters;
import ru.practicum.explorewithme.dto.event.fitler.EventSearchFilters;
import ru.practicum.explorewithme.dto.event.state.EventStates;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.exception.ValidationException;
import ru.practicum.explorewithme.mapper.EventMapper;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.EventCommentCount;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.repository.CategoryRepository;
import ru.practicum.explorewithme.repository.CommentRepository;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.UserRepository;
import ru.practicum.explorewithme.service.EventService;
import ru.practicum.explorewithme.utils.DateTimeUtils;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventServiceImpl implements EventService {

	private static final String NOT_FOUND_EVENT_BY_ID = "Событие c ID = %d не найдено";

	private static final String NOT_FOUND_CATEGORY_BY_ID = "Категория c ID = %d не найдена";

	private static final String NOT_FOUND_USER_BY_ID = "Пользователь c ID = %d не найден";

	private final EventRepository eventRepository;

	private final UserRepository userRepository;

	private final CategoryRepository categoryRepository;

	private final CommentRepository commentRepository;

	private final StatsClient statsClient;

	@Override
	public EventFullDto getPublished(Long eventId) {
		Event event = eventRepository.findByIdAndState(eventId, EventStates.PUBLISHED)
				.orElseThrow(() -> new NotFoundException(NOT_FOUND_EVENT_BY_ID, eventId));
		updateView(event);
		return getEventsFullDto(List.of(event)).getFirst();
	}

	@Override
	public List<EventShortDto> get(Long userId, Integer from, Integer size) {
		getUser(userId);
		Pageable pageable = Pageable.ofSize(size).withPage(from / size);
		Page<Event> eventsByPage = eventRepository.findAllByInitiatorId(userId, pageable);
		List<Event> events = eventsByPage.hasContent() ? eventsByPage.getContent() : Collections.emptyList();
		return getEventsShortDto(events);
	}

	@Override
	public List<EventFullDto> get(EventAdminSearchFilters filters) {
		Pageable pageable = Pageable.ofSize(filters.getSize()).withPage(filters.getFrom() / filters.getSize());
		Page<Event> eventsByPage = eventRepository.findAllByAdminFilters(
				filters.getUsers(),
				filters.getStates(),
				filters.getCategories(),
				filters.getRangeStart(),
				filters.getRangeEnd(),
				pageable
		);
		List<Event> events = eventsByPage.hasContent() ? eventsByPage.getContent() : Collections.emptyList();
		return getEventsFullDto(events);
	}

	@Override
	public List<EventShortDto> get(EventSearchFilters filters) {
		if (filters.getRangeEnd() != null && filters.getRangeStart() != null
				&& filters.getRangeEnd().isBefore(filters.getRangeStart())) {
			throw new ValidationException("Дата начала не должна быть позже даты окончания");
		}
		Pageable pageable = Pageable.ofSize(filters.getSize()).withPage(filters.getFrom() / filters.getSize());
		List<Event> events = eventRepository.findAllByFilters(
				filters.getText(),
				filters.getPaid(),
				filters.getCategories(),
				filters.getRangeStart(),
				filters.getRangeEnd(),
				filters.getOnlyAvailable(),
				filters.getSort().name(),
				EventStates.PUBLISHED,
				LocalDateTime.now(),
				pageable
		);
		return getEventsShortDto(events);
	}

	@Override
	public EventFullDto getById(Long userId, Long eventId) {
		getUser(userId);
		Event event = eventRepository.findByIdAndInitiatorId(eventId, userId)
				.orElseThrow(() -> new NotFoundException(NOT_FOUND_EVENT_BY_ID, eventId));
		return getEventsFullDto(List.of(event)).getFirst();
	}

	@Override
	public EventFullDto add(Long userId, NewEventDto newEventDto) {
		User user = getUser(userId);
		Category category = getCategory(newEventDto.getCategory());
		Event event = EventMapper.fromNewEventDto(newEventDto, user, category);
		return EventMapper.toEventFullDto(eventRepository.saveAndFlush(event), 0L);
	}

	@Override
	public EventFullDto update(Long userId, Long eventId, UpdateEventUserRequest updateEventUserRequest) {
		getUser(userId);
		Event event = getEvent(eventId);
		Category category = null;
		if (updateEventUserRequest.getCategory() != null) {
			category = getCategory(updateEventUserRequest.getCategory());
		}
		event = EventMapper.fromUpdateEventUserRequest(userId, event, updateEventUserRequest, category);
		event = eventRepository.saveAndFlush(event);
		return getEventsFullDto(List.of(event)).getFirst();
	}

	@Override
	public EventFullDto update(Long eventId, UpdateEventAdminRequest updateEventAdminRequest) {
		Event event = getEvent(eventId);
		Category category = null;
		if (updateEventAdminRequest.getCategory() != null) {
			category = getCategory(updateEventAdminRequest.getCategory());
		}
		event = EventMapper.fromUpdateEventAdminRequest(event, updateEventAdminRequest, category);
		event = eventRepository.saveAndFlush(event);
		return getEventsFullDto(List.of(event)).getFirst();
	}

	private User getUser(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException(NOT_FOUND_USER_BY_ID, userId));
	}

	private Event getEvent(Long eventId) {
		return eventRepository.findById(eventId)
				.orElseThrow(() -> new NotFoundException(NOT_FOUND_EVENT_BY_ID, eventId));
	}

	private Category getCategory(Long categoryId) {
		return categoryRepository.findById(categoryId)
				.orElseThrow(() -> new NotFoundException(NOT_FOUND_CATEGORY_BY_ID, categoryId));
	}

	private void updateView(Event event) {
		Long views = getViews(event.getId());
		if (!Objects.equals(event.getViews(), views)) {
			event.setViews(views);
			eventRepository.saveAndFlush(event);
		}
	}

	private Long getViews(Long id) {
		List<ViewStatsDto> stats = statsClient.getStats(
				"1970-01-01 00:00:00",
				DateTimeUtils.dateTimeToString(LocalDateTime.now()),
				List.of("/events/" + id),
				true);
		return stats.isEmpty() ? 0L : stats.getFirst().getHits();
	}

	private Map<Long, Long> getEventCommentCount(List<Event> events) {
		Set<Long> eventIds = events.stream()
				.map(Event::getId)
				.collect(Collectors.toSet());
		return commentRepository.getEventsCommentCount(eventIds).stream()
				.collect(Collectors.toMap(EventCommentCount::getEventId, EventCommentCount::getCommentCount));
	}

	private List<EventShortDto> getEventsShortDto(List<Event> events) {
		Map<Long, Long> eventCommentCounts = getEventCommentCount(events);
		return events.stream()
				.map(event ->
						EventMapper.toEventShortDto(event, eventCommentCounts
								.getOrDefault(event.getId(), 0L))
				)
				.toList();
	}

	private List<EventFullDto> getEventsFullDto(List<Event> events) {
		Map<Long, Long> eventCommentCounts = getEventCommentCount(events);
		return events.stream()
				.map(event ->
						EventMapper.toEventFullDto(event, eventCommentCounts
								.getOrDefault(event.getId(), 0L))
				)
				.toList();
	}

}

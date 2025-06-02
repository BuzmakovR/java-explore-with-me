package ru.practicum.explorewithme.service.impl;

import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.event.state.EventStates;
import ru.practicum.explorewithme.dto.eventRequest.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.eventRequest.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.eventRequest.ParticipationRequestDto;
import ru.practicum.explorewithme.dto.eventRequest.ParticipationRequestStatus;
import ru.practicum.explorewithme.exception.ConditionsNotMetException;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.mapper.ParticipationRequestMapper;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.ParticipationRequest;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.ParticipationRequestRepository;
import ru.practicum.explorewithme.repository.UserRepository;
import ru.practicum.explorewithme.service.ParticipationRequestService;

import java.util.List;
import java.util.Objects;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class ParticipationRequestServiceImpl implements ParticipationRequestService {

	private static final String NOT_FOUND_REQUEST_BY_ID = "Запрос на участие c ID = %d не найден";

	private static final String NOT_FOUND_EVENT_BY_ID = "Событие c ID = %d не найдено";

	private static final String NOT_FOUND_USER_BY_ID = "Пользователь c ID = %d не найден";

	private final ParticipationRequestRepository participationRequestRepository;

	private final EventRepository eventRepository;

	private final UserRepository userRepository;

	@Override
	public List<ParticipationRequestDto> getByUserId(Long userId) {
		getUser(userId);
		return participationRequestRepository.findAllByRequesterId(userId)
				.stream()
				.map(ParticipationRequestMapper::toParticipationRequestDto)
				.toList();
	}

	@Override
	public List<ParticipationRequestDto> getAllByInitiatorAndEvent(Long userId, Long eventId) {
		getUser(userId);
		Event event = getEvent(eventId);

		if (!event.getInitiator().getId().equals(userId)) {
			throw new ConditionsNotMetException("Просмотр заявок на событие доступен только инициатору события");
		}
		return participationRequestRepository.findAllByEventId(eventId)
				.stream()
				.map(ParticipationRequestMapper::toParticipationRequestDto)
				.toList();
	}

	@Override
	@Transactional
	public EventRequestStatusUpdateResult updateEventRequestsStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
		getUser(userId);
		Event event = getEvent(eventId);

		if (!event.getInitiator().getId().equals(userId)) {
			throw new ConditionsNotMetException("Обновление статуса заявок на событие доступно только инициатору события");
		}
		if (!event.getRequestModeration()) {
			throw new ConditionsNotMetException("Обновление статуса заявок на событие доступно только для модерируемых события");
		}
		Set<Long> requestIds = eventRequestStatusUpdateRequest.getRequestIds();
		List<ParticipationRequest> participationRequests = participationRequestRepository.findAllByIdInAndEventId(requestIds, eventId);

		for (ParticipationRequest participationRequest : participationRequests) {
			switch (eventRequestStatusUpdateRequest.getStatus()) {
				case CONFIRMED -> {
					if (participationRequest.getStatus() != ParticipationRequestStatus.PENDING) {
						throw new ConditionsNotMetException("Обновление статуса заявок на событие доступно только для находящихся в состоянии ожидания");
					}
					if (event.getParticipantLimit() == 0L || event.getConfirmedRequests() < event.getParticipantLimit()) {
						participationRequest.setStatus(ParticipationRequestStatus.CONFIRMED);
						event.setConfirmedRequests(event.getConfirmedRequests() + 1);
					} else if (participationRequests.size() == 1) {
						throw new ConditionsNotMetException("Достигнут лимит подтвержденных заявок на участие в событии");
					} else {
						participationRequest.setStatus(ParticipationRequestStatus.REJECTED);
					}
				}
				case REJECTED -> {
					ParticipationRequestStatus oldStatus = participationRequest.getStatus();
					participationRequest.setStatus(ParticipationRequestStatus.REJECTED);
					if (oldStatus == ParticipationRequestStatus.CONFIRMED) {
						throw new ConditionsNotMetException("Нельзя отменить уже принятую заявку");
					}
				}
			}
		}
		participationRequestRepository.saveAllAndFlush(participationRequests);
		eventRepository.saveAndFlush(event);

		participationRequests = participationRequestRepository.findAllByEventId(eventId);
		return EventRequestStatusUpdateResult.builder()
				.confirmedRequests(participationRequests
						.stream()
						.filter(participationRequest -> ParticipationRequestStatus.CONFIRMED == participationRequest.getStatus())
						.map(ParticipationRequestMapper::toParticipationRequestDto)
						.collect(Collectors.toSet()))
				.rejectedRequests(participationRequests
						.stream()
						.filter(participationRequest -> ParticipationRequestStatus.REJECTED == participationRequest.getStatus())
						.map(ParticipationRequestMapper::toParticipationRequestDto)
						.collect(Collectors.toSet()))
				.build();
	}

	@Override
	@Transactional
	public ParticipationRequestDto add(Long userId, Long eventId) {
		User user = getUser(userId);
		Event event = getEvent(eventId);

		if (user.equals(event.getInitiator())) {
			throw new ConditionsNotMetException("Нельзя добавить запрос за участие в своем событии");
		}
		if (event.getState() != EventStates.PUBLISHED) {
			throw new ConditionsNotMetException("Нельзя отправить запрос на неопубликованное событие");
		}
		if (event.getParticipantLimit() != 0 && Objects.equals(event.getParticipantLimit(), event.getConfirmedRequests())) {
			throw new ConditionsNotMetException("Достигнут лимит заявок на участие в событии");
		}
		if (!participationRequestRepository.findAllByRequesterIdAndEventId(userId, eventId).isEmpty()) {
			throw new ConflictException("Нельзя добавить повторный запрос за участие");
		}
		ParticipationRequest participationRequest = participationRequestRepository
				.saveAndFlush(ParticipationRequestMapper
						.toParticipationRequest(user, event));

		if (!event.getRequestModeration()) {
			event.setConfirmedRequests(event.getConfirmedRequests() + 1);
			eventRepository.save(event);
		}
		return ParticipationRequestMapper.toParticipationRequestDto(participationRequest);
	}

	@Override
	@Transactional
	public ParticipationRequestDto cancel(Long userId, Long requestId) {
		getUser(userId);
		ParticipationRequest participationRequest = participationRequestRepository.findById(requestId)
				.orElseThrow(() -> new NotFoundException(NOT_FOUND_REQUEST_BY_ID, requestId));

		if (!participationRequest.getRequester().getId().equals(userId)) {
			throw new ConditionsNotMetException("Нельзя отменить заявку на участие для другого пользователя");
		}

		if (participationRequest.getStatus() == ParticipationRequestStatus.CONFIRMED) {
			Event event = participationRequest.getEvent();
			event.setConfirmedRequests(event.getConfirmedRequests() - 1);
			eventRepository.save(event);
		}
		participationRequest.setStatus(ParticipationRequestStatus.CANCELED);
		return ParticipationRequestMapper.toParticipationRequestDto(
				participationRequestRepository.saveAndFlush(participationRequest));
	}

	private User getUser(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException(NOT_FOUND_USER_BY_ID, userId));
	}

	private Event getEvent(Long eventId) {
		return eventRepository.findById(eventId)
				.orElseThrow(() -> new NotFoundException(NOT_FOUND_EVENT_BY_ID, eventId));
	}
}

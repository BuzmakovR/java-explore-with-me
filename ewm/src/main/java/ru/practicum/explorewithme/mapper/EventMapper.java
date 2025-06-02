package ru.practicum.explorewithme.mapper;

import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.Location;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.dto.event.UpdateEventAdminRequest;
import ru.practicum.explorewithme.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.dto.event.state.EventStates;
import ru.practicum.explorewithme.exception.ConditionsNotMetException;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.ValidationException;
import ru.practicum.explorewithme.model.Category;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.User;

import java.time.LocalDateTime;

public class EventMapper {

	public static Event copyEvent(Event event) {
		return Event.builder()
				.id(event.getId())
				.initiator(event.getInitiator())
				.annotation(event.getAnnotation())
				.title(event.getTitle())
				.description(event.getDescription())
				.eventDate(event.getEventDate())
				.category(event.getCategory())
				.lat(event.getLat())
				.lon(event.getLon())
				.confirmedRequests(event.getConfirmedRequests())
				.paid(event.getPaid())
				.participantLimit(event.getParticipantLimit())
				.state(event.getState())
				.createdOn(event.getCreatedOn())
				.publishedOn(event.getPublishedOn())
				.requestModeration(event.getRequestModeration())
				.views(event.getViews())
				.build();
	}

	public static Event fromNewEventDto(NewEventDto newEventDto, User user, Category category) {
		return Event.builder()
				.initiator(user)
				.annotation(newEventDto.getAnnotation())
				.title(newEventDto.getTitle())
				.category(category)
				.description(newEventDto.getDescription())
				.eventDate(newEventDto.getEventDate())
				.lat(newEventDto.getLocation().getLat())
				.lon(newEventDto.getLocation().getLon())
				.paid(newEventDto.getPaid() != null && newEventDto.getPaid())
				.participantLimit(newEventDto.getParticipantLimit() == null ? 0L : newEventDto.getParticipantLimit())
				.confirmedRequests(0L)
				.state(EventStates.PENDING)
				.createdOn(LocalDateTime.now())
				.requestModeration(newEventDto.getRequestModeration() == null || newEventDto.getRequestModeration())
				.build();
	}

	public static Event fromUpdateEventUserRequest(Long userId, Event event,
												   UpdateEventUserRequest updateEventUserRequest,
												   Category category) {
		Event newEvent = copyEvent(event);
		if (!event.getInitiator().getId().equals(userId)) {
			throw new ConditionsNotMetException("Событие может редактировать только его создатель");
		}
		if (newEvent.getState() == EventStates.PUBLISHED) {
			throw new ConditionsNotMetException("Недоступно редактирование события в статусе: " + newEvent.getState());
		}
		LocalDateTime localDateTimeNow = LocalDateTime.now();
		if (updateEventUserRequest.getEventDate() != null) {
			LocalDateTime localDateTimeUpdateEvenDateDeadline = localDateTimeNow.plusHours(2);
			if (localDateTimeUpdateEvenDateDeadline.isAfter(updateEventUserRequest.getEventDate())) {
				throw new ValidationException("Дата и время события не может быть указано раньше, чем через два часа от текущего момента");
			}
			newEvent.setEventDate(updateEventUserRequest.getEventDate());
		}

		if (updateEventUserRequest.getStateAction() != null) {
			switch (updateEventUserRequest.getStateAction()) {
				case SEND_TO_REVIEW -> newEvent.setState(EventStates.PENDING);
				case CANCEL_REVIEW -> newEvent.setState(EventStates.CANCELED);
			}
		}
		if (updateEventUserRequest.getAnnotation() != null && !updateEventUserRequest.getAnnotation().isBlank()) {
			newEvent.setAnnotation(updateEventUserRequest.getAnnotation());
		}
		if (updateEventUserRequest.getTitle() != null && !updateEventUserRequest.getTitle().isBlank()) {
			newEvent.setTitle(updateEventUserRequest.getTitle());
		}
		if (updateEventUserRequest.getDescription() != null && !updateEventUserRequest.getDescription().isBlank()) {
			newEvent.setDescription(updateEventUserRequest.getDescription());
		}
		if (category != null) {
			newEvent.setCategory(category);
		}
		if (updateEventUserRequest.getLocation() != null) {
			if (updateEventUserRequest.getLocation().getLat() != null) {
				newEvent.setLat(updateEventUserRequest.getLocation().getLat());
			}
			if (updateEventUserRequest.getLocation().getLon() != null) {
				newEvent.setLon(updateEventUserRequest.getLocation().getLon());
			}
		}
		if (updateEventUserRequest.getPaid() != null) {
			newEvent.setPaid(updateEventUserRequest.getPaid());
		}
		if (updateEventUserRequest.getParticipantLimit() != null) {
			newEvent.setParticipantLimit(updateEventUserRequest.getParticipantLimit());
		}
		if (updateEventUserRequest.getRequestModeration() != null) {
			newEvent.setRequestModeration(updateEventUserRequest.getRequestModeration());
		}
		return newEvent;
	}

	public static Event fromUpdateEventAdminRequest(Event event,
													UpdateEventAdminRequest updateEventAdminRequest,
													Category category) {
		Event newEvent = copyEvent(event);

		if (newEvent.getState() == EventStates.PUBLISHED) {
			throw new ConditionsNotMetException("Недоступно редактирование события в статусе: " + newEvent.getState());
		}
		LocalDateTime localDateTimeNow = LocalDateTime.now();
		LocalDateTime localDateTimeUpdateDeadline = localDateTimeNow.plusHours(1);

		if (event.getState() == EventStates.PUBLISHED
				&& localDateTimeUpdateDeadline.isAfter(event.getPublishedOn())) {
			throw new ValidationException("Дата начала изменяемого события должна быть не ранее чем за час от даты публикации");
		}
		if (updateEventAdminRequest.getEventDate() != null) {
			LocalDateTime localDateTimeUpdateEvenDateDeadline = localDateTimeNow.plusHours(2);
			if (localDateTimeUpdateEvenDateDeadline.isAfter(updateEventAdminRequest.getEventDate())) {
				throw new ValidationException("Дата и время события не может быть указано раньше, чем через два часа от текущего момента");
			}
			newEvent.setEventDate(updateEventAdminRequest.getEventDate());
		}

		if (updateEventAdminRequest.getStateAction() != null) {
			switch (updateEventAdminRequest.getStateAction()) {
				case PUBLISH_EVENT -> {
					if (newEvent.getState() != EventStates.PENDING) {
						throw new ConflictException("Событие можно опубликовать, только если оно в состоянии ожидания публикации. Текущее состояние: " + newEvent.getState());
					}
					newEvent.setState(EventStates.PUBLISHED);
				}
				case REJECT_EVENT -> {
					if (newEvent.getState() != EventStates.PENDING) {
						throw new ConflictException("Событие можно отклонить, только если оно еще не опубликовано. Текущее состояние: " + newEvent.getState());
					}
					newEvent.setState(EventStates.CANCELED);
				}
			}
		}
		if (updateEventAdminRequest.getAnnotation() != null && !updateEventAdminRequest.getAnnotation().isBlank()) {
			newEvent.setAnnotation(updateEventAdminRequest.getAnnotation());
		}
		if (updateEventAdminRequest.getTitle() != null && !updateEventAdminRequest.getTitle().isBlank()) {
			newEvent.setTitle(updateEventAdminRequest.getTitle());
		}
		if (updateEventAdminRequest.getDescription() != null && !updateEventAdminRequest.getDescription().isBlank()) {
			newEvent.setDescription(updateEventAdminRequest.getDescription());
		}
		if (category != null) {
			newEvent.setCategory(category);
		}
		if (updateEventAdminRequest.getLocation() != null) {
			if (updateEventAdminRequest.getLocation().getLat() != null) {
				newEvent.setLat(updateEventAdminRequest.getLocation().getLat());
			}
			if (updateEventAdminRequest.getLocation().getLon() != null) {
				newEvent.setLon(updateEventAdminRequest.getLocation().getLon());
			}
		}
		if (updateEventAdminRequest.getPaid() != null) {
			newEvent.setPaid(updateEventAdminRequest.getPaid());
		}
		if (updateEventAdminRequest.getParticipantLimit() != null) {
			newEvent.setParticipantLimit(updateEventAdminRequest.getParticipantLimit());
		}
		if (updateEventAdminRequest.getRequestModeration() != null) {
			newEvent.setRequestModeration(updateEventAdminRequest.getRequestModeration());
		}
		return newEvent;
	}

	public static EventShortDto toEventShortDto(Event event) {
		return EventShortDto.builder()
				.id(event.getId())
				.initiator(UserMapper.toUserShortDto(event.getInitiator()))
				.annotation(event.getAnnotation())
				.title(event.getTitle())
				.eventDate(event.getEventDate())
				.category(CategoryMapper.toCategoryDto(event.getCategory()))
				.paid(event.getPaid())
				.confirmedRequests(event.getConfirmedRequests())
				.views(event.getViews())
				.build();
	}

	public static EventFullDto toEventFullDto(Event event) {
		return EventFullDto.builder()
				.id(event.getId())
				.initiator(UserMapper.toUserShortDto(event.getInitiator()))
				.annotation(event.getAnnotation())
				.title(event.getTitle())
				.description(event.getDescription())
				.eventDate(event.getEventDate())
				.location(Location.builder()
						.lat(event.getLat())
						.lon(event.getLon())
						.build())
				.category(CategoryMapper.toCategoryDto(event.getCategory()))
				.paid(event.getPaid())
				.participantLimit(event.getParticipantLimit())
				.confirmedRequests(event.getConfirmedRequests())
				.views(event.getViews())
				.requestModeration(event.getRequestModeration())
				.state(event.getState())
				.createdOn(event.getCreatedOn())
				.publishedOn(event.getPublishedOn())
				.build();
	}
}

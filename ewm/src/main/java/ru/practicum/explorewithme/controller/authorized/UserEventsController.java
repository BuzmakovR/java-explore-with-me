package ru.practicum.explorewithme.controller.authorized;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.dto.event.NewEventDto;
import ru.practicum.explorewithme.dto.event.UpdateEventUserRequest;
import ru.practicum.explorewithme.dto.eventRequest.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.eventRequest.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.eventRequest.ParticipationRequestDto;
import ru.practicum.explorewithme.service.EventService;
import ru.practicum.explorewithme.service.ParticipationRequestService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class UserEventsController {

	private final EventService eventService;

	private final ParticipationRequestService participationRequestService;

	@GetMapping
	public List<EventShortDto> get(@PathVariable Long userId,
								   @RequestParam(required = false, defaultValue = "0") Integer from,
								   @RequestParam(required = false, defaultValue = "10") Integer size) {
		log.info("Поступил запрос GET /users/{userId}/events. Параметры запроса: userId = {}; from = {}; size = {}", userId, from, size);
		return eventService.get(userId, from, size);
	}

	@GetMapping("/{eventId}")
	public EventFullDto get(@PathVariable Long userId,
							@PathVariable Long eventId) {
		log.info("Поступил запрос GET /users/{userId}/events/{eventId}. Параметры запроса: userId = {}; eventId = {}", userId, eventId);
		return eventService.getById(userId, eventId);
	}

	@GetMapping("/{eventId}/requests")
	public List<ParticipationRequestDto> getEventRequests(@PathVariable Long userId,
														  @PathVariable Long eventId) {
		log.info("Поступил запрос GET /users/{userId}/events/{eventId}/requests. Параметры запроса: userId = {}; eventId = {}", userId, eventId);
		return participationRequestService.getAllByInitiatorAndEvent(userId, eventId);
	}

	@PatchMapping("/{eventId}/requests")
	public EventRequestStatusUpdateResult updateEventRequestsStatus(@PathVariable Long userId,
																	@PathVariable Long eventId,
																	@RequestBody EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest) {
		log.info("Поступил запрос PATCH /users/{userId}/events/{eventId}/requests. Параметры запроса: userId = {}; eventId = {}; eventRequestStatusUpdateRequest = {}", userId, eventId, eventRequestStatusUpdateRequest);
		return participationRequestService.updateEventRequestsStatus(userId, eventId, eventRequestStatusUpdateRequest);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public EventFullDto add(@PathVariable Long userId,
							@Valid @RequestBody NewEventDto newEventDto) {
		log.info("Поступил запрос POST /users/{userId}/events. Параметры запроса: userId = {}; newUserRequest = {}", userId, newEventDto);
		return eventService.add(userId, newEventDto);
	}

	@PatchMapping("/{eventId}")
	public EventFullDto update(@PathVariable Long userId,
							   @PathVariable Long eventId,
							   @Valid @RequestBody UpdateEventUserRequest updateEventUserRequest) {
		log.info("Поступил запрос POST /users/{userId}/events/{eventId}. Параметры запроса: userId = {}; eventId = {}; updateEventUserRequest = {};", userId, eventId, updateEventUserRequest);
		return eventService.update(userId, eventId, updateEventUserRequest);
	}


}

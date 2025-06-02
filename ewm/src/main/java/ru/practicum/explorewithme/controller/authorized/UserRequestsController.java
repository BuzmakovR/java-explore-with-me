package ru.practicum.explorewithme.controller.authorized;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.eventRequest.ParticipationRequestDto;
import ru.practicum.explorewithme.service.ParticipationRequestService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/requests")
@RequiredArgsConstructor
public class UserRequestsController {

	private final ParticipationRequestService participationRequestService;

	@GetMapping
	public List<ParticipationRequestDto> get(@PathVariable Long userId) {
		log.info("Поступил запрос GET /users/{userId}/requests. Параметры запроса: userId = {}", userId);
		return participationRequestService.getByUserId(userId);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public ParticipationRequestDto add(@PathVariable Long userId,
									   @RequestParam Long eventId) {
		log.info("Поступил запрос POST /users/{userId}/requests. Параметры запроса: userId = {}; eventId = {}", userId, eventId);
		return participationRequestService.add(userId, eventId);
	}

	@PatchMapping("/{requestId}/cancel")
	public ParticipationRequestDto cancel(@PathVariable Long userId,
										  @PathVariable Long requestId) {
		log.info("Поступил запрос PATCH /users/{userId}/requests/{requestId}/cancel. Параметры запроса: userId = {}; eventId = {}", userId, requestId);
		return participationRequestService.cancel(userId, requestId);
	}
}


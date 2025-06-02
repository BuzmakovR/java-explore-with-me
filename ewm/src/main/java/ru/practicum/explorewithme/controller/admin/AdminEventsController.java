package ru.practicum.explorewithme.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.UpdateEventAdminRequest;
import ru.practicum.explorewithme.dto.event.fitler.EventAdminSearchFilters;
import ru.practicum.explorewithme.dto.event.state.EventStates;
import ru.practicum.explorewithme.service.EventService;
import ru.practicum.explorewithme.utils.DateTimeUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventsController {

	private final EventService eventService;

	@GetMapping
	public List<EventFullDto> get(@RequestParam(required = false) List<Long> users,
								  @RequestParam(required = false) List<EventStates> states,
								  @RequestParam(required = false) List<Long> categories,
								  @RequestParam(required = false) @DateTimeFormat(pattern = DateTimeUtils.DATE_TIME_PATTERN) LocalDateTime rangeStart,
								  @RequestParam(required = false) @DateTimeFormat(pattern = DateTimeUtils.DATE_TIME_PATTERN) LocalDateTime rangeEnd,
								  @RequestParam(required = false, defaultValue = "0") Integer from,
								  @RequestParam(required = false, defaultValue = "10") Integer size) {
		EventAdminSearchFilters filters = EventAdminSearchFilters.builder()
				.users(users)
				.states(states)
				.categories(categories)
				.rangeStart(rangeStart)
				.rangeEnd(rangeEnd)
				.from(from)
				.size(size)
				.build();
		log.info("Поступил запрос GET /admin/events. Параметры запроса: filters = {}", filters);
		return eventService.get(filters);
	}

	@PatchMapping("/{eventId}")
	public EventFullDto update(@PathVariable Long eventId,
							   @Valid @RequestBody UpdateEventAdminRequest updateEventAdminRequest) {
		log.info("Поступил запрос GET /admin/events/{eventId}. Параметры запроса: eventId = {}", eventId);
		return eventService.update(eventId, updateEventAdminRequest);
	}
}

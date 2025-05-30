package ru.practicum.explorewithme.controller.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.event.EventFullDto;
import ru.practicum.explorewithme.dto.event.fitler.EventSearchFilters;
import ru.practicum.explorewithme.dto.event.fitler.EventSearchOrder;
import ru.practicum.explorewithme.dto.event.EventShortDto;
import ru.practicum.explorewithme.service.EventService;
import ru.practicum.explorewithme.utils.DateTimeUtils;

import java.time.LocalDateTime;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class EventsController {

	private final EventService eventService;

	@GetMapping("/{eventId}")
	public EventFullDto get(@PathVariable long eventId) {
		log.info("Поступил запрос GET /events/{eventId}. Параметры запроса: eventId = {}", eventId);
		return eventService.getPublished(eventId);
	}

	@GetMapping
	public List<EventShortDto> get(@RequestParam(required = false) String text,
								   @RequestParam(required = false) List<Long> categories,
								   @RequestParam(required = false) Boolean paid,
								   @RequestParam(required = false) @DateTimeFormat(pattern = DateTimeUtils.DATE_TIME_PATTERN) LocalDateTime rangeStart,
								   @RequestParam(required = false) @DateTimeFormat(pattern = DateTimeUtils.DATE_TIME_PATTERN) LocalDateTime rangeEnd,
								   @RequestParam(defaultValue = "false") Boolean onlyAvailable,
								   @RequestParam(defaultValue = "EVENT_DATE") String sort,
								   @RequestParam(defaultValue = "0") Integer from,
								   @RequestParam(defaultValue = "10") Integer size) {
		EventSearchFilters filters = EventSearchFilters.builder()
				.text(text)
				.categories(categories)
				.paid(paid)
				.rangeStart(rangeStart)
				.rangeEnd(rangeEnd)
				.onlyAvailable(onlyAvailable)
				.sort(EventSearchOrder.valueOf(sort))
				.from(from)
				.size(size)
				.build();

		log.info("Поступил запрос GET /events. Параметры запроса: filters = {}", filters);
		return eventService.get(filters);
	}
}

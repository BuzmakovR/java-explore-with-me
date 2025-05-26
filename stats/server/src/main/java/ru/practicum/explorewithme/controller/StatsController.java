package ru.practicum.explorewithme.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.dto.ViewStatsDto;
import ru.practicum.explorewithme.service.EndpointHitService;

import java.time.LocalDateTime;
import java.util.List;

@RestController
@Slf4j
@RequiredArgsConstructor
public class StatsController {

	private final EndpointHitService endpointHitService;

	@PostMapping("/hit")
	public ResponseEntity<EndpointHitDto> hit(@RequestBody EndpointHitDto endpointHitDto) {
		log.info("Поступил запрос POST /hit. Тело запроса: {}", endpointHitDto);
		return new ResponseEntity<>(endpointHitService.addHit(endpointHitDto), HttpStatus.CREATED);
	}

	@GetMapping("/stats")
	public ResponseEntity<List<ViewStatsDto>> stats(@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
													@RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
													@RequestParam(required = false) List<String> uris,
													@RequestParam(defaultValue = "false") Boolean unique) {
		log.info("Поступил запрос GET /stats. Параметры запроса: start = {}; end = {}; unique = {}; uris = {}", start, end, uris, unique);
		return new ResponseEntity<>(endpointHitService.getStats(start, end, uris, unique), HttpStatus.OK);
	}

}

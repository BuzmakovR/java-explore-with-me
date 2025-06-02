package ru.practicum.explorewithme.controller.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.service.CompilationService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/compilations")
@RequiredArgsConstructor
public class CompilationsController {

	private final CompilationService compilationService;

	@GetMapping
	public List<CompilationDto> get(@RequestParam(required = false) Boolean pinned,
									@RequestParam(defaultValue = "0") Integer from,
									@RequestParam(defaultValue = "10") Integer size) {
		log.info("Поступил запрос GET /compilations. Параметры запроса: pinned = {}; from = {}; size = {}", pinned, from, size);
		return compilationService.get(pinned, from, size);
	}

	@GetMapping("/{compilationId}")
	public CompilationDto get(@PathVariable Long compilationId) {
		log.info("Поступил запрос GET /compilations/{compilationId}. Параметры запроса: compilationId = {}", compilationId);
		return compilationService.get(compilationId);
	}
}

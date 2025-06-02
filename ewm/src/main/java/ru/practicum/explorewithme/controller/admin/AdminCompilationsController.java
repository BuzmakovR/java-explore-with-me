package ru.practicum.explorewithme.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explorewithme.service.CompilationService;

@Slf4j
@RestController
@RequestMapping("/admin/compilations")
@RequiredArgsConstructor
public class AdminCompilationsController {

	private final CompilationService compilationService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CompilationDto add(@RequestBody @Valid NewCompilationDto newCompilationDto) {
		log.info("Поступил запрос POST /admin/compilations. Параметры запроса: newCompilationDto = {}", newCompilationDto);
		return compilationService.add(newCompilationDto);
	}

	@PatchMapping("/{compilationId}")
	public CompilationDto update(@PathVariable Long compilationId,
								 @RequestBody @Valid UpdateCompilationRequest updateCompilationRequest) {
		log.info("Поступил запрос PATCH /admin/compilations/{compilationId}. Параметры запроса: compilationId = {}; updateCompilationRequest = {}", compilationId, updateCompilationRequest);
		return compilationService.update(compilationId, updateCompilationRequest);
	}

	@DeleteMapping("/{compilationId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long compilationId) {
		log.info("Поступил запрос DELETE /admin/compilations/{compilationId}. Параметры запроса: compilationId = {}", compilationId);
		compilationService.delete(compilationId);
	}

}

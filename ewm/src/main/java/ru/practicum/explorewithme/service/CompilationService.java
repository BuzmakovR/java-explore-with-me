package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.compilation.UpdateCompilationRequest;

import java.util.List;

public interface CompilationService {

	List<CompilationDto> get(Boolean pinned, Integer from, Integer size);

	CompilationDto get(Long compilationId);

	CompilationDto add(NewCompilationDto newCompilationDto);

	CompilationDto update(Long compilationId, UpdateCompilationRequest updateCompilationRequest);

	void delete(Long compilationId);
}

package ru.practicum.explorewithme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.dto.compilation.UpdateCompilationRequest;
import ru.practicum.explorewithme.exception.ConditionsNotMetException;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.mapper.CompilationMapper;
import ru.practicum.explorewithme.model.Compilation;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.EventCommentCount;
import ru.practicum.explorewithme.repository.CommentRepository;
import ru.practicum.explorewithme.repository.CompilationRepository;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.service.CompilationService;

import java.util.Collections;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class CompilationServiceImpl implements CompilationService {

	private static final String NOT_FOUND_COMPILATION_BY_ID = "Подборка c ID = %d не найдена";

	private final CompilationRepository compilationRepository;

	private final EventRepository eventRepository;

	private final CommentRepository commentRepository;

	@Override
	public List<CompilationDto> get(Boolean pinned, Integer from, Integer size) {
		Pageable pageable = Pageable.ofSize(size).withPage(from / size);
		Page<Compilation> compilationByPage = compilationRepository.findAll(pageable);
		List<Compilation> compilations = compilationByPage.hasContent() ? compilationByPage.getContent() : Collections.emptyList();
		return getCompilationsDto(compilations);
	}

	@Override
	public CompilationDto get(Long compilationId) {
		Compilation compilation = getCompilation(compilationId);
		return getCompilationsDto(List.of(compilation)).getFirst();
	}

	@Override
	public CompilationDto add(NewCompilationDto newCompilationDto) {
		validateTitle(null, newCompilationDto.getTitle());
		Set<Event> events = new HashSet<>();
		if (newCompilationDto.getEvents() != null && !newCompilationDto.getEvents().isEmpty()) {
			events = eventRepository.findAllByIdIn(newCompilationDto.getEvents());
		}
		Compilation compilation = compilationRepository.save(CompilationMapper.fromNewCompilationDto(newCompilationDto, events));
		return getCompilationsDto(List.of(compilation)).getFirst();
	}

	@Override
	public CompilationDto update(Long compilationId, UpdateCompilationRequest updateCompilationRequest) {
		Compilation compilation = getCompilation(compilationId);

		if (updateCompilationRequest.getTitle() != null && !updateCompilationRequest.getTitle().isBlank()) {
			validateTitle(compilationId, updateCompilationRequest.getTitle());
			compilation.setTitle(updateCompilationRequest.getTitle());
		}

		if (updateCompilationRequest.getEvents() != null) {
			Set<Event> events = new HashSet<>();
			if (!updateCompilationRequest.getEvents().isEmpty()) {
				events = new HashSet<>(eventRepository.findAllByIdIn(updateCompilationRequest.getEvents()));
			}
			compilation.setEvents(events);
		}
		if (updateCompilationRequest.getPinned() != null) {
			compilation.setPinned(updateCompilationRequest.getPinned());
		}
		compilation = compilationRepository.saveAndFlush(compilation);
		return getCompilationsDto(List.of(compilation)).getFirst();
	}

	@Override
	public void delete(Long compilationId) {
		Compilation compilation = getCompilation(compilationId);
		if (!compilation.getEvents().isEmpty()) {
			throw new ConflictException("Нельзя удалить не пустую подборку");
		}
		compilationRepository.deleteById(compilationId);
	}

	private Compilation getCompilation(Long compilationId) {
		return compilationRepository.findById(compilationId)
				.orElseThrow(() -> new NotFoundException(NOT_FOUND_COMPILATION_BY_ID, compilationId));
	}

	private void validateTitle(Long compilationId, String title) {
		List<Compilation> compilations = compilationRepository.findByTitleIgnoreCase(title);
		if (!compilations.isEmpty()
				&& (compilationId == null
				|| compilations.stream().anyMatch(c -> compilationId.equals(c.getId())))) {
			throw new ConditionsNotMetException("Подборка с таким названием уже существует: " + title);
		}
	}

	private Map<Long, Long> getEventCommentCount(List<Event> events) {
		Set<Long> eventIds = events.stream()
				.map(Event::getId)
				.collect(Collectors.toSet());
		return getEventCommentCount(eventIds);
	}

	private Map<Long, Long> getEventCommentCount(Set<Long> eventIds) {
		return commentRepository.getEventsCommentCount(eventIds).stream()
				.collect(Collectors.toMap(EventCommentCount::getEventId, EventCommentCount::getCommentCount));
	}

	private List<CompilationDto> getCompilationsDto(List<Compilation> compilations) {
		Set<Event> events = new HashSet<>();
		compilations.forEach(compilation -> {
			events.addAll(compilation.getEvents());
		});
		Map<Long, Long> eventCommentCounts = getEventCommentCount(events.stream().toList());
		return compilations.stream()
				.map(compilation -> CompilationMapper.toCompilationDto(compilation, eventCommentCounts))
				.toList();
	}
}

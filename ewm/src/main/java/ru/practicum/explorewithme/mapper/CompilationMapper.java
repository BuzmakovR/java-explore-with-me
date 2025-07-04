package ru.practicum.explorewithme.mapper;

import ru.practicum.explorewithme.dto.compilation.CompilationDto;
import ru.practicum.explorewithme.dto.compilation.NewCompilationDto;
import ru.practicum.explorewithme.model.Compilation;
import ru.practicum.explorewithme.model.Event;

import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class CompilationMapper {

	public static Compilation fromNewCompilationDto(NewCompilationDto newCompilationDto, Set<Event> events) {
		return Compilation.builder()
				.title(newCompilationDto.getTitle())
				.pinned(newCompilationDto.getPinned() != null ? newCompilationDto.getPinned() : false)
				.events(events)
				.build();
	}

	public static CompilationDto toCompilationDto(Compilation compilation, Map<Long, Long> eventCommentCounts) {
		return CompilationDto.builder()
				.id(compilation.getId())
				.title(compilation.getTitle())
				.pinned(compilation.getPinned())
				.events(compilation.getEvents()
						.stream()
						.map(event ->
								EventMapper.toEventShortDto(event, eventCommentCounts
										.getOrDefault(event.getId(), 0L))
						)
						.collect(Collectors.toSet()))
				.build();
	}
}

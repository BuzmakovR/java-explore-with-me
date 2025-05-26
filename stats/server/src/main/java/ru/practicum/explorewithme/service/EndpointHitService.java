package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.dto.ViewStatsDto;

import java.time.LocalDateTime;
import java.util.List;

public interface EndpointHitService {

	EndpointHitDto addHit(EndpointHitDto endpointHitDto);

	List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}

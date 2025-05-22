package ru.practicum.explorewithme.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.dto.ViewStatsDto;
import ru.practicum.explorewithme.mapper.EndpointHitMapper;
import ru.practicum.explorewithme.mapper.ViewStatsMapper;
import ru.practicum.explorewithme.repository.EndpointHitRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class EndpointHitServiceImpl implements EndpointHitService {

	private final EndpointHitRepository endpointHitRepository;

	@Override
	public EndpointHitDto addHit(EndpointHitDto endpointHitDto) {
		return EndpointHitMapper.toEndpointHitDto(
				endpointHitRepository.saveAndFlush(
						EndpointHitMapper.fromEndpointHitDto(endpointHitDto)));
	}

	@Override
	public List<ViewStatsDto> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {
		return unique
				? endpointHitRepository.getStatsUniqie(start, end, uris).stream()
				.map(ViewStatsMapper::toViewStatsDto)
				.toList()
				: endpointHitRepository.getStats(start, end, uris).stream()
				.map(ViewStatsMapper::toViewStatsDto)
				.toList();
	}
}

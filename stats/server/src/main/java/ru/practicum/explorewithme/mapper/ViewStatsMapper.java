package ru.practicum.explorewithme.mapper;

import ru.practicum.explorewithme.dto.ViewStatsDto;
import ru.practicum.explorewithme.model.ViewStats;

public class ViewStatsMapper {

	public static ViewStatsDto toViewStatsDto(ViewStats viewStats) {
		return ViewStatsDto.builder()
				.app(viewStats.getApp())
				.uri(viewStats.getUri())
				.hits(viewStats.getHits())
				.build();
	}
}

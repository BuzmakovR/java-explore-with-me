package ru.practicum.explorewithme.mapper;

import ru.practicum.explorewithme.dto.EndpointHitDto;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.utils.DateTimeUtils;

public class EndpointHitMapper {

	public static EndpointHitDto toEndpointHitDto(EndpointHit endpointHit) {
		return EndpointHitDto.builder()
				.app(endpointHit.getApp())
				.uri(endpointHit.getUri())
				.ip(endpointHit.getIp())
				.timestamp(DateTimeUtils.dateTimeToString(endpointHit.getTimestamp()))
				.build();
	}

	public static EndpointHit fromEndpointHitDto(EndpointHitDto endpointHitDto) {
		return EndpointHit.builder()
				.app(endpointHitDto.getApp())
				.uri(endpointHitDto.getUri())
				.ip(endpointHitDto.getIp())
				.timestamp(DateTimeUtils.stringToDateTime(endpointHitDto.getTimestamp()))
				.build();
	}
}

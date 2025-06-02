package ru.practicum.explorewithme.mapper;

import ru.practicum.explorewithme.dto.eventRequest.ParticipationRequestDto;
import ru.practicum.explorewithme.dto.eventRequest.ParticipationRequestStatus;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.ParticipationRequest;
import ru.practicum.explorewithme.model.User;

import java.time.LocalDateTime;

public class ParticipationRequestMapper {

	public static ParticipationRequest toParticipationRequest(User user, Event event) {
		return ParticipationRequest.builder()
				.requester(user)
				.event(event)
				.status(event.getParticipantLimit() == 0 ? ParticipationRequestStatus.CONFIRMED : ParticipationRequestStatus.PENDING)
				.created(LocalDateTime.now())
				.build();
	}

	public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
		return ParticipationRequestDto.builder()
				.id(participationRequest.getId())
				.requester(participationRequest.getRequester().getId())
				.event(participationRequest.getEvent().getId())
				.status(participationRequest.getStatus())
				.created(participationRequest.getCreated())
				.build();
	}
}

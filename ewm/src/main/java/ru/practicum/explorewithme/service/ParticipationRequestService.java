package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.eventRequest.EventRequestStatusUpdateRequest;
import ru.practicum.explorewithme.dto.eventRequest.EventRequestStatusUpdateResult;
import ru.practicum.explorewithme.dto.eventRequest.ParticipationRequestDto;

import java.util.List;

public interface ParticipationRequestService {

	List<ParticipationRequestDto> getByUserId(Long userId);

	List<ParticipationRequestDto> getAllByInitiatorAndEvent(Long userId, Long eventId);

	EventRequestStatusUpdateResult updateEventRequestsStatus(Long userId, Long eventId, EventRequestStatusUpdateRequest eventRequestStatusUpdateRequest);

	ParticipationRequestDto add(Long userId, Long eventId);

	ParticipationRequestDto cancel(Long userId, Long requestId);

}

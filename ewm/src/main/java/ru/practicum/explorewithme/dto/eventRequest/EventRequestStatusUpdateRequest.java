package ru.practicum.explorewithme.dto.eventRequest;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Set;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventRequestStatusUpdateRequest {

	private Set<Long> requestIds;

	private ParticipationRequestStatus status;

}

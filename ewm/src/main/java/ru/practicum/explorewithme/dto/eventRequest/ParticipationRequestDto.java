package ru.practicum.explorewithme.dto.eventRequest;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.utils.DateTimeUtils;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class ParticipationRequestDto {

	private Long id;

	private Long requester;

	private Long event;

	private ParticipationRequestStatus status;

	@JsonFormat(pattern = DateTimeUtils.DATE_TIME_PATTERN)
	private LocalDateTime created;
}

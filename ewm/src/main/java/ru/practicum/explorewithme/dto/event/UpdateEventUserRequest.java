package ru.practicum.explorewithme.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.dto.event.state.UserEventStateAction;
import ru.practicum.explorewithme.utils.DateTimeUtils;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class UpdateEventUserRequest {

	@Size(min = 20, max = 2000)
	private String annotation;

	@Size(min = 3, max = 120)
	private String title;

	@Size(min = 20, max = 7000)
	private String description;

	@JsonFormat(pattern = DateTimeUtils.DATE_TIME_PATTERN)
	private LocalDateTime eventDate;

	private Location location;

	private Long category;

	private Boolean paid;

	@PositiveOrZero
	private Long participantLimit;

	private Boolean requestModeration;

	private UserEventStateAction stateAction;
}

package ru.practicum.explorewithme.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.practicum.explorewithme.dto.category.CategoryDto;
import ru.practicum.explorewithme.dto.event.state.EventStates;
import ru.practicum.explorewithme.dto.user.UserShortDto;
import ru.practicum.explorewithme.utils.DateTimeUtils;

import java.time.LocalDateTime;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class EventFullDto {

	private Long id;

	private UserShortDto initiator;

	private String annotation;

	private String title;

	private String description;

	@JsonFormat(pattern = DateTimeUtils.DATE_TIME_PATTERN)
	private LocalDateTime eventDate;

	private Location location;

	private CategoryDto category;

	private Boolean paid;

	private Long participantLimit;

	private Long confirmedRequests;

	private Long views;

	private Boolean requestModeration;

	private EventStates state;

	@JsonFormat(pattern = DateTimeUtils.DATE_TIME_PATTERN)
	private LocalDateTime createdOn;

	@JsonFormat(pattern = DateTimeUtils.DATE_TIME_PATTERN)
	private LocalDateTime publishedOn;
}

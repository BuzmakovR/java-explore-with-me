package ru.practicum.explorewithme.dto.event;

import com.fasterxml.jackson.annotation.JsonFormat;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;
import jakarta.validation.constraints.Size;
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
public class NewEventDto {

	@NotBlank
	@Size(min = 20, max = 2000)
	private String annotation;

	@NotBlank
	@Size(min = 3, max = 120)
	private String title;

	@NotNull
	private Long category;

	@NotBlank
	@Size(min = 20, max = 7000)
	private String description;

	@NotNull
	@Future
	@JsonFormat(shape = JsonFormat.Shape.STRING, pattern = DateTimeUtils.DATE_TIME_PATTERN)
	private LocalDateTime eventDate;

	@NotNull
	private Location location;

	private Boolean paid;

	@PositiveOrZero
	private Long participantLimit;

	private Boolean requestModeration;

}

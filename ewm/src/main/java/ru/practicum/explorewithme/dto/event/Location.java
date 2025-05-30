package ru.practicum.explorewithme.dto.event;

import jakarta.validation.constraints.Min;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Location {

	@NotNull
	@Min(-90)
	@Min(90)
	private Double lat;

	@NotNull
	@Min(-180)
	@Min(180)
	private Double lon;
}

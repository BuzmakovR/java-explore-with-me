package ru.practicum.explorewithme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class EndpointHitDto {

	@NotBlank
	private String app;

	@NotBlank
	private String uri;

	@NotBlank
	private String ip;

	@NotBlank
	private String timestamp;
}
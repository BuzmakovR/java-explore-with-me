package ru.practicum.explorewithme.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
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
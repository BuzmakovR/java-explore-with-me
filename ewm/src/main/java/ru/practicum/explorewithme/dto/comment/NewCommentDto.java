package ru.practicum.explorewithme.dto.comment;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class NewCommentDto {

	@NotNull
	private Long eventId;

	@NotBlank
	@Size(min = 10, max = 2000)
	private String text;
}

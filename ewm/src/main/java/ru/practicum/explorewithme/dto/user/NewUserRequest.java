package ru.practicum.explorewithme.dto.user;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class NewUserRequest {

	@NotBlank()
	@Size(min = 2, max = 250)
	private String name;

	@Email()
	@NotBlank()
	@Size(min = 6, max = 254)
	private String email;
}

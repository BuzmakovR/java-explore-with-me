package ru.practicum.explorewithme.dto.category;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class NewCategoryDto {

	@NotBlank(message = "Параметр name должен быть заполнен")
	@Size(min = 1, max = 50, message = "Некорректная длина параметра name")
	private String name;
}

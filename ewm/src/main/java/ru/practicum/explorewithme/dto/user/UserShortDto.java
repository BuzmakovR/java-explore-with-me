package ru.practicum.explorewithme.dto.user;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class UserShortDto {

	private Long id;

	private String name;
}

package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;

import java.util.List;

public interface UserService {

	List<UserDto> get(List<Long> ids, Integer from, Integer size);

	UserDto add(NewUserRequest newUserRequest);

	void delete(Long id);
}

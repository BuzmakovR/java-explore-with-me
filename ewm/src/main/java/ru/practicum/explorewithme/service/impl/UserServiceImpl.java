package ru.practicum.explorewithme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.exception.ConflictException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.exception.ValidationException;
import ru.practicum.explorewithme.mapper.UserMapper;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.repository.UserRepository;
import ru.practicum.explorewithme.service.UserService;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

	private final UserRepository userRepository;

	@Override
	public List<UserDto> get(List<Long> ids, Integer from, Integer size) {
		List<User> users;
		if (ids != null && !ids.isEmpty()) {
			users = userRepository.findUsersAllByIdIn(ids);
		} else {
			Pageable pageable = Pageable.ofSize(size).withPage(from / size);
			Page<User> usersByPage = userRepository.findAll(pageable);
			users = usersByPage.hasContent() ? usersByPage.getContent() : Collections.emptyList();
		}
		return users.stream()
				.map(UserMapper::toUserDto)
				.toList();
	}

	@Override
	public UserDto add(NewUserRequest newUserRequest) {
		User user = UserMapper.fromNewUserRequest(newUserRequest);
		validate(user);
		return UserMapper.toUserDto(userRepository.saveAndFlush(user));
	}

	@Override
	public void delete(Long id) {
		if (!userRepository.existsById(id)) {
			throw new NotFoundException("Пользователь c ID = %d не найден", id);
		}
		userRepository.deleteById(id);
	}

	public void validate(User user) {
		if (user.getEmail() == null || user.getEmail().isBlank()) {
			throw new ValidationException("Email пользователя должен быть заполнен");
		}
		Optional<User> userOptional = userRepository.findAllByEmail(user.getEmail());
		if (userOptional.isPresent() && !Objects.equals(user.getId(), userOptional.get().getId())) {
			throw new ConflictException("Нарушена уникальность пользователей по email");
		}
	}
}

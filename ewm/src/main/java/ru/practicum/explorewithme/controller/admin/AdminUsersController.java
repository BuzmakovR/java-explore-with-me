package ru.practicum.explorewithme.controller.admin;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.user.NewUserRequest;
import ru.practicum.explorewithme.dto.user.UserDto;
import ru.practicum.explorewithme.service.UserService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping(path = "/admin/users")
@RequiredArgsConstructor
public class AdminUsersController {

	private final UserService userService;

	@GetMapping
	public List<UserDto> get(@RequestParam(required = false) List<Long> ids,
							 @RequestParam(required = false, defaultValue = "0") Integer from,
							 @RequestParam(required = false, defaultValue = "10") Integer size) {
		log.info("Поступил запрос GET /admin/users. Параметры запроса: ids = {}; from = {}; size = {}", ids, from, size);
		return userService.get(ids, from, size);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public UserDto add(@Valid @RequestBody NewUserRequest newUserRequest) {
		log.info("Поступил запрос POST /admin/users. Параметры запроса: newUserRequest = {}", newUserRequest);
		return userService.add(newUserRequest);
	}

	@DeleteMapping("/{userId}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long userId) {
		log.info("Поступил запрос DELETE /admin/users/{userId}. Параметры запроса: userId = {}", userId);
		userService.delete(userId);
	}
}

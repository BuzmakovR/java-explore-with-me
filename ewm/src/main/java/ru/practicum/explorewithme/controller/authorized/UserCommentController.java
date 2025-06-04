package ru.practicum.explorewithme.controller.authorized;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.dto.comment.UpdateCommentRequest;
import ru.practicum.explorewithme.service.CommentService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/users/{userId}/comments")
@RequiredArgsConstructor
public class UserCommentController {

	private final CommentService commentService;

	@GetMapping
	public List<CommentDto> get(@PathVariable Long userId) {
		log.info("Поступил запрос GET /users/{userId}/comments. Параметры запроса: userId = {}", userId);
		return commentService.getByAuthorId(userId);
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public CommentDto add(@PathVariable Long userId,
						  @Valid @RequestBody NewCommentDto newCommentDto) {
		log.info("Поступил запрос POST /users/{userId}/comments. Параметры запроса: userId = {}; newCommentDto = {}", userId, newCommentDto);
		return commentService.add(userId, newCommentDto);
	}

	@PatchMapping("/{commentId}")
	public CommentDto update(@PathVariable Long userId,
							 @PathVariable Long commentId,
							 @Valid @RequestBody UpdateCommentRequest updateCommentRequest) {
		log.info("Поступил запрос PATCH /users/{userId}/comments/{commentId}. Параметры запроса: userId = {}; commentId = {}; updateCommentRequest = {}", userId, commentId, updateCommentRequest);
		return commentService.update(userId, commentId, updateCommentRequest);
	}

	@DeleteMapping("/{commentId}")
	public void delete(@PathVariable Long userId,
					   @PathVariable Long commentId) {
		log.info("Поступил запрос DELETE /users/{userId}/comments/{commentId}. Параметры запроса: userId = {}; commentId = {}", userId, commentId);
		commentService.delete(userId, commentId);
	}
}

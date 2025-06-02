package ru.practicum.explorewithme.controller.admin;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.service.CommentService;

@Slf4j
@RestController
@RequestMapping("/admin/comments/{commentId}")
@RequiredArgsConstructor
public class AdminCommentController {

	private final CommentService commentService;

	@GetMapping
	public CommentDto get(@PathVariable Long commentId) {
		log.info("Поступил запрос GET /admin/comments/{commentId}. Параметры запроса: commentId = {}", commentId);
		return commentService.getById(commentId);
	}

	@DeleteMapping
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long commentId) {
		log.info("Поступил запрос DELETE /admin/comments/{commentId}. Параметры запроса: commentId = {}", commentId);
		commentService.delete(commentId);
	}
}

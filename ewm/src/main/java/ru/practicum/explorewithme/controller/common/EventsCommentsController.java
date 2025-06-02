package ru.practicum.explorewithme.controller.common;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.service.CommentService;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/events/{eventId}/comments")
@RequiredArgsConstructor
public class EventsCommentsController {

	private final CommentService commentService;

	@GetMapping
	public List<CommentDto> get(@PathVariable Long eventId,
								@RequestParam(defaultValue = "0") Integer from,
								@RequestParam(defaultValue = "10") Integer size) {
		log.info("Поступил запрос GET /events/{eventId}/comments. Параметры запроса: eventId = {}; from = {}; size = {}", eventId, from, size);
		return commentService.getByEventId(eventId, from, size);
	}

	@GetMapping("/{commentId}")
	public CommentDto get(@PathVariable Long eventId,
						  @PathVariable Long commentId) {
		log.info("Поступил запрос GET /events/{eventId}/comments/{commentId}. Параметры запроса: eventId = {}; commentId = {}", eventId, commentId);
		return commentService.getById(commentId);
	}
}
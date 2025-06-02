package ru.practicum.explorewithme.service;

import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.dto.comment.UpdateCommentRequest;

import java.util.List;

public interface CommentService {

	CommentDto getById(Long commentId);

	List<CommentDto> getByAuthorId(Long authorId);

	List<CommentDto> getByEventId(Long eventId, Integer from, Integer size);

	CommentDto add(Long authorId, NewCommentDto newCommentDto);

	CommentDto update(Long authorId, Long commentId, UpdateCommentRequest updateCommentRequest);

	void delete(Long authorId, Long commentId);

	void delete(Long commentId);
}

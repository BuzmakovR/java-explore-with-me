package ru.practicum.explorewithme.service.impl;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.dto.comment.UpdateCommentRequest;
import ru.practicum.explorewithme.exception.ConditionsNotMetException;
import ru.practicum.explorewithme.exception.NotFoundException;
import ru.practicum.explorewithme.mapper.CommentMapper;
import ru.practicum.explorewithme.model.Comment;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.User;
import ru.practicum.explorewithme.repository.CommentRepository;
import ru.practicum.explorewithme.repository.EventRepository;
import ru.practicum.explorewithme.repository.UserRepository;
import ru.practicum.explorewithme.service.CommentService;

import java.util.Collections;
import java.util.List;

@Service
@RequiredArgsConstructor
public class CommentServiceImpl implements CommentService {

	private static final String NOT_FOUND_COMMENT_BY_ID = "Комментарий c ID = %d не найдена";

	private static final String NOT_FOUND_USER_BY_ID = "Пользователь c ID = %d не найден";

	private static final String NOT_FOUND_EVENT_BY_ID = "Событие c ID = %d не найдено";

	private final CommentRepository commentRepository;

	private final UserRepository userRepository;

	private final EventRepository eventRepository;

	@Override
	public CommentDto getById(Long commentId) {
		return CommentMapper.toCommentDto(
				commentRepository.findById(commentId)
						.orElseThrow(() -> new NotFoundException(NOT_FOUND_COMMENT_BY_ID, commentId)));
	}

	@Override
	public List<CommentDto> getByAuthorId(Long authorId) {
		List<Comment> comments = commentRepository.findAllByAuthorId(authorId);
		return comments.stream()
				.map(CommentMapper::toCommentDto)
				.toList();
	}

	@Override
	public List<CommentDto> getByEventId(Long eventId, Integer from, Integer size) {
		getEvent(eventId);
		Pageable pageable = Pageable.ofSize(size).withPage(from / size);
		Page<Comment> commentsByPage = commentRepository.findAllByEventId(eventId, pageable);
		List<Comment> comments = commentsByPage.hasContent() ? commentsByPage.getContent() : Collections.emptyList();
		return comments.stream()
				.map(CommentMapper::toCommentDto)
				.toList();
	}

	@Override
	public CommentDto add(Long authorId, NewCommentDto newCommentDto) {
		User user = getUser(authorId);
		Event event = getEvent(newCommentDto.getEventId());
		Comment comment = CommentMapper.fromNewCommentDto(newCommentDto, user, event);
		return CommentMapper.toCommentDto(
				commentRepository.saveAndFlush(comment)
		);
	}

	@Override
	public CommentDto update(Long authorId, Long commentId, UpdateCommentRequest updateCommentRequest) {
		getUser(authorId);
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new NotFoundException(NOT_FOUND_COMMENT_BY_ID, commentId));

		if (!comment.getAuthor().getId().equals(authorId)) {
			throw new ConditionsNotMetException("Недоступно редактирование комментариев от других пользователей");
		}
		comment = CommentMapper.fromUpdateCommentRequest(updateCommentRequest, comment);
		return CommentMapper.toCommentDto(
				commentRepository.saveAndFlush(comment)
		);
	}

	@Override
	public void delete(Long authorId, Long commentId) {
		getUser(authorId);
		Comment comment = commentRepository.findById(commentId)
				.orElseThrow(() -> new NotFoundException(NOT_FOUND_COMMENT_BY_ID, commentId));

		if (!comment.getAuthor().getId().equals(authorId)) {
			throw new ConditionsNotMetException("Недоступно удаление комментариев от других пользователей");
		}
		commentRepository.deleteById(commentId);
	}

	@Override
	public void delete(Long commentId) {
		commentRepository.findById(commentId)
				.orElseThrow(() -> new NotFoundException(NOT_FOUND_COMMENT_BY_ID, commentId));
		commentRepository.deleteById(commentId);
	}

	private User getUser(Long userId) {
		return userRepository.findById(userId)
				.orElseThrow(() -> new NotFoundException(NOT_FOUND_USER_BY_ID, userId));
	}

	private Event getEvent(Long eventId) {
		return eventRepository.findById(eventId)
				.orElseThrow(() -> new NotFoundException(NOT_FOUND_EVENT_BY_ID, eventId));
	}
}

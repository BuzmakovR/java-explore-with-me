package ru.practicum.explorewithme.mapper;

import ru.practicum.explorewithme.dto.comment.CommentDto;
import ru.practicum.explorewithme.dto.comment.NewCommentDto;
import ru.practicum.explorewithme.dto.comment.UpdateCommentRequest;
import ru.practicum.explorewithme.exception.ConditionsNotMetException;
import ru.practicum.explorewithme.model.Comment;
import ru.practicum.explorewithme.model.Event;
import ru.practicum.explorewithme.model.User;

import java.time.LocalDateTime;

public class CommentMapper {

	public static Comment fromNewCommentDto(NewCommentDto newCommentDto, User user, Event event) {
		return Comment.builder()
				.author(user)
				.event(event)
				.text(newCommentDto.getText())
				.created(LocalDateTime.now())
				.build();
	}

	public static Comment fromUpdateCommentRequest(UpdateCommentRequest updateCommentRequest, Comment comment) {
		LocalDateTime localDateTimeUpdateDeadline = comment.getCreated().plusHours(1);
		if (LocalDateTime.now().isAfter(localDateTimeUpdateDeadline)) {
			throw new ConditionsNotMetException("Редактирование комментариев доступно только в течение часа после его добавления");
		}
		return Comment.builder()
				.id(comment.getId())
				.author(comment.getAuthor())
				.event(comment.getEvent())
				.text(updateCommentRequest.getText())
				.created(comment.getCreated())
				.edited(LocalDateTime.now())
				.build();
	}

	public static CommentDto toCommentDto(Comment comment, Long commentCountToEvent) {
		return CommentDto.builder()
				.id(comment.getId())
				.author(UserMapper.toUserShortDto(comment.getAuthor()))
				.event(EventMapper.toEventShortDto(comment.getEvent(), commentCountToEvent))
				.text(comment.getText())
				.created(comment.getCreated())
				.edited(comment.getEdited())
				.build();
	}
}

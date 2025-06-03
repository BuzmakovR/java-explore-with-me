package ru.practicum.explorewithme.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.model.Comment;
import ru.practicum.explorewithme.model.EventCommentCount;

import java.util.List;
import java.util.Set;

@Repository
public interface CommentRepository extends JpaRepository<Comment, Long> {

	List<Comment> findAllByAuthorId(Long authorId);

	Page<Comment> findAllByEventId(Long eventId, Pageable pageable);

	long countByEventId(Long eventId);

	@Query("SELECT new ru.practicum.explorewithme.model.EventCommentCount(e.id, COUNT(c.id)) " +
			"FROM Comment c " +
			"JOIN c.event e " +
			"WHERE e.id IN (:eventIds) " +
			"GROUP BY e.id")
	List<EventCommentCount> getEventsCommentCount(Set<Long> eventIds);
}

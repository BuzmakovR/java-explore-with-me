package ru.practicum.explorewithme.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.dto.event.state.EventStates;
import ru.practicum.explorewithme.model.Event;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;
import java.util.Set;

@Repository
public interface EventRepository extends JpaRepository<Event, Long> {

	Set<Event> findAllByIdIn(Set<Long> ids);

	Set<Event> findAllByCategoryId(Long id);

	Optional<Event> findByIdAndState(Long id, EventStates eventState);

	Optional<Event> findByIdAndInitiatorId(Long id, Long initiatorId);

	Page<Event> findAllByInitiatorId(Long initiatorId, Pageable pageable);

	@Query("SELECT e FROM Event e " +
			"WHERE (:users IS NULL OR e.initiator.id IN :users) " +
			"AND (:states IS NULL OR e.state IN :states) " +
			"AND (:categories IS NULL OR e.category.id IN :categories) " +
			"AND (CAST(:rangeStart as DATE) IS NULL OR e.eventDate >= :rangeStart) " +
			"AND (CAST(:rangeEnd as DATE) IS NULL OR e.eventDate <= :rangeEnd) " +
			"ORDER BY e.eventDate DESC"
	)
	Page<Event> findAllByAdminFilters(@Param("users") List<Long> users,
									  @Param("states") List<EventStates> states,
									  @Param("categories") List<Long> categories,
									  @Param("rangeStart") LocalDateTime rangeStart,
									  @Param("rangeEnd") LocalDateTime rangeEnd,
									  Pageable pageable
	);

	@Query("SELECT e FROM Event e " +
			"WHERE " +
			"(" +
			"	LOWER(e.annotation) LIKE LOWER(CONCAT('%', COALESCE(:text, ''), '%')) " +
			"	OR LOWER(e.description) LIKE LOWER(CONCAT('%', COALESCE(:text, ''), '%')) " +
			"	OR LOWER(e.title) LIKE LOWER(CONCAT('%', COALESCE(:text, ''), '%')) " +
			"	OR :text IS NULL " +
			") " +
			"AND (:paid IS NULL OR e.paid = :paid) " +
			"AND (:categories IS NULL OR e.category.id IN :categories) " +
			"AND (" +
			"	(CAST(:rangeStart as DATE) IS NULL " +
			"		AND CAST(:rangeEnd as DATE) IS NULL " +
			"		AND e.eventDate > :currentTime) " +
			"	OR (CAST(:rangeStart as DATE) IS NOT NULL " +
			"		AND e.eventDate >= :rangeStart) " +
			"	OR (CAST(:rangeEnd as DATE) IS NOT NULL " +
			"		AND e.eventDate <= :rangeEnd) " +
			") " +
			"AND (:onlyAvailable IS NULL " +
			"     OR (:onlyAvailable = TRUE AND (e.participantLimit = 0 OR e.participantLimit > e.confirmedRequests)) " +
			"     OR :onlyAvailable = FALSE) " +
			"AND e.state = :state " +
			"ORDER BY " +
			"CASE WHEN :sort = 'EVENT_DATE' THEN e.eventDate END ASC, " +
			"CASE WHEN :sort = 'VIEWS' THEN e.views END DESC")
	List<Event> findAllByFilters(
			@Param("text") String text,
			@Param("paid") Boolean paid,
			@Param("categories") List<Long> categories,
			@Param("rangeStart") LocalDateTime rangeStart,
			@Param("rangeEnd") LocalDateTime rangeEnd,
			@Param("onlyAvailable") Boolean onlyAvailable,
			@Param("sort") String sort,
			@Param("state") EventStates state,
			@Param("currentTime") LocalDateTime currentTime,
			Pageable pageable
	);
}

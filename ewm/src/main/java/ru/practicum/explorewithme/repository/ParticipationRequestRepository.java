package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.model.ParticipationRequest;

import java.util.List;
import java.util.Set;

@Repository
public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {

	List<ParticipationRequest> findAllByRequesterId(Long userId);

	List<ParticipationRequest> findAllByEventId(Long eventId);

	List<ParticipationRequest> findAllByRequesterIdAndEventId(Long userId, Long eventId);

	List<ParticipationRequest> findAllByIdInAndEventId(Set<Long> ids, Long eventId);

}

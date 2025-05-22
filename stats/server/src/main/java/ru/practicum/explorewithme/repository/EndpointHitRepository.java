package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import ru.practicum.explorewithme.model.EndpointHit;
import ru.practicum.explorewithme.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface EndpointHitRepository extends JpaRepository<EndpointHit, Long> {

	@Query("SELECT new ru.practicum.explorewithme.model.ViewStats(eh.app, eh.uri, COUNT(eh.ip)) " +
			"FROM EndpointHit eh " +
			"WHERE eh.timestamp BETWEEN :startDate and :endDate " +
			"AND (:uris IS NULL OR eh.uri IN (:uris)) " +
			"GROUP BY eh.app, eh.uri " +
			"ORDER BY COUNT(eh.ip) DESC")
	List<ViewStats> getStats(@Param("startDate") LocalDateTime start,
							 @Param("endDate") LocalDateTime end,
							 @Param("uris") List<String> uris);

	@Query("SELECT new ru.practicum.explorewithme.model.ViewStats(eh.app, eh.uri, COUNT(DISTINCT eh.ip)) " +
			"FROM EndpointHit eh " +
			"WHERE eh.timestamp BETWEEN :startDate and :endDate " +
			"AND (:uris IS NULL OR eh.uri IN (:uris)) " +
			"GROUP BY eh.app, eh.uri " +
			"ORDER BY COUNT(DISTINCT eh.ip) DESC")
	List<ViewStats> getStatsUniqie(@Param("startDate") LocalDateTime start,
								   @Param("endDate") LocalDateTime end,
								   @Param("uris") List<String> uris);
}

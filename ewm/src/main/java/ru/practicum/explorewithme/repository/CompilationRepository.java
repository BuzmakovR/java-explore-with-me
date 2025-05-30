package ru.practicum.explorewithme.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.data.domain.Pageable;
import ru.practicum.explorewithme.model.Compilation;

import java.util.List;

@Repository
public interface CompilationRepository extends JpaRepository<Compilation, Long> {

	List<Compilation> findByTitleIgnoreCase(String title);

	@Query("SELECT c FROM Compilation c " +
			"WHERE (:pinned IS NULL OR c.pinned = :pinned)")
	List<Compilation> findCompilations(@Param("pinned") Boolean pinned, Pageable pageable);
}

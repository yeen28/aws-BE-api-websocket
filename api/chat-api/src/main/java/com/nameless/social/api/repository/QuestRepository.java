package com.nameless.social.api.repository;

import com.nameless.social.core.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {
	Optional<Quest> findByName(final String name);

	int countByClubIdAndCreatedAtBetween(long clubId, LocalDateTime start, LocalDateTime end);
	List<Quest> findByCreatedAtBetween(LocalDateTime startOfDay, LocalDateTime endOfDay);
}

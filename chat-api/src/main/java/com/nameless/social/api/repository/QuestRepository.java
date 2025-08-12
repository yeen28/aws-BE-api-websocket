package com.nameless.social.api.repository;

import com.nameless.social.core.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {
	Optional<Quest> findByName(final String name);
}

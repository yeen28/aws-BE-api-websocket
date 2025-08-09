package com.nameless.social.api.repository;

import com.nameless.social.core.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {
	List<Quest> findAllByClubId(final long clubId);
	List<Quest> findAllByUserId(final long userId);
}

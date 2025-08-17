package com.nameless.social.api.repository;

import com.nameless.social.core.entity.UserQuest;
import com.nameless.social.core.entity.UserQuestId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserQuestRepository extends JpaRepository<UserQuest, UserQuestId> {
	// 존재 여부 확인 (LIMIT 1)
	boolean existsByIdUserId(long userId);

	// 해당 userId로 모든 UserQuest 삭제
	void deleteByIdUserId(long userId);

	int deleteByIdUserIdAndQuestId(long userId, long questId);

	List<UserQuest> findByIdUserId(long userId);
	List<UserQuest> findAllByIsSuccess(boolean flag);
}

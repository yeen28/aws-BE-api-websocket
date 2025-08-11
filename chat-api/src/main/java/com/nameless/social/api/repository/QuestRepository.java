package com.nameless.social.api.repository;

import com.nameless.social.core.entity.Quest;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.time.LocalDateTime;
import java.util.List;

@Repository
public interface QuestRepository extends JpaRepository<Quest, Long> {
	// 전체 개수
	long countByUserId(Long userId);

	// 날짜 범위 조건 포함
	long countByUserIdAndCreatedAtBetween(Long userId, LocalDateTime start, LocalDateTime end);

	// 통계용 그룹 조회 (예: 상태별 퀘스트 개수)
	@Query("SELECT q.status, COUNT(q) " +
			"FROM Quest q " +
			"WHERE q.user.id = :userId " +
			"GROUP BY q.status")
	List<Object[]> countQuestsByStatus(@Param("userId") Long userId);
}

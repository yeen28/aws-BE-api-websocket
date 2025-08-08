package com.nameless.social.api.repository;

import com.nameless.social.core.entity.UserClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserClubRepository extends JpaRepository<UserClub, Long> {
	// 존재 여부 확인 (LIMIT 1)
	boolean existsByIdUserId(long userId);

	// 해당 userId로 모든 UserClub 삭제
	void deleteByIdUserId(long userId);

	@Query("SELECT uc FROM UserClub uc WHERE uc.user.id = :userId AND uc.club.id IN :clubIds")
	List<UserClub> findByUserClubsInClubIds(@Param("userId") long userId, @Param("clubIds") List<Long> clubIds);

	@Query("DELETE FROM UserClub uc WHERE uc.user.id = :userId AND uc.club.id = :clubId")
	int deleteByUserIdAndClubId(@Param("userId") long userId, @Param("clubId") Long clubId);
}

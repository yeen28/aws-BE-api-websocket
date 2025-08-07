package com.nameless.social.api.repository;

import com.nameless.social.core.entity.UserClub;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserClubRepository extends JpaRepository<UserClub, Long> {
	@Query("SELECT uc FROM UserClub uc WHERE uc.user.id = :userId AND uc.club.id IN :clubIds")
	List<UserClub> findUserClubsInGroup(@Param("userId") Long userId, @Param("clubIds") List<Long> clubIds);
}

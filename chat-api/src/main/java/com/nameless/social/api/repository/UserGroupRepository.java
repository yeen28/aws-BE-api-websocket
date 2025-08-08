package com.nameless.social.api.repository;

import com.nameless.social.core.entity.UserGroup;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, Long> {
	// 존재 여부 확인 (LIMIT 1)
	boolean existsByIdUserId(long userId);

	// 해당 userId로 모든 UserGroup 삭제
	void deleteByIdUserId(long userId);
}

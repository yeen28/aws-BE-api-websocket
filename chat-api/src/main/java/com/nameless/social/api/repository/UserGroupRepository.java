package com.nameless.social.api.repository;

import com.nameless.social.core.entity.UserGroup;
import com.nameless.social.core.entity.UserGroupId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserGroupRepository extends JpaRepository<UserGroup, UserGroupId> {
	List<UserGroup> findAllByIdUserId(final long userId);
	long countByIdGroupId(final long groupId);
	// 존재 여부 확인 (LIMIT 1)
	boolean existsByIdUserId(final long userId);
	// 해당 userId로 모든 UserGroup 삭제
	void deleteByIdUserId(final long userId);
}

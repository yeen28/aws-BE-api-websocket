package com.nameless.social.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;

@Entity
@Getter
@Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class UserGroup extends BaseTimeEntity {
	@EmbeddedId // 복합키(Composite key)를 위한 JPA 방식
	private UserGroupId id; // user_id + group_id 두 개의 컬럼이 합쳐져서 PK가 됩니다.

	@ManyToOne
	@MapsId("userId") // 기본 키에 포함된 필드(userId)를 이 필드(user)가 사용한다는 뜻
	// 이 관계의 외래 키(foreign key)가 기본 키로도 사용되고 있다는 의미
	private User user;

	@ManyToOne
	@MapsId("groupId")
	private Group group;

	private LocalDateTime joinedAt;

	public UserGroup(User user, Group group) {
		this.id = new UserGroupId(user.getId(), group.getId());
		this.user = user;
		this.group = group;
		this.joinedAt = LocalDateTime.now();
	}
}

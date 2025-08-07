package com.nameless.social.core.entity;

import jakarta.persistence.*;
import lombok.Getter;

@Getter
@Entity
public class UserClub extends BaseTimeEntity {
	@EmbeddedId
	private UserClubId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("clubId")
	private Club club;

	@Column(name = "role")
	private String role; // 유저의 역할 (e.g., admin, member)

	@Column(name = "last_read_message_id")
	private Long lastReadMessageId; // 마지막으로 읽은 메시지 ID

	@Column(name = "notification_settings")
	private String notificationSettings; // 알림 설정 (e.g., on, off)

	public UserClub(User user, Club club) {
		this.user = user;
		this.club = club;
	}
}

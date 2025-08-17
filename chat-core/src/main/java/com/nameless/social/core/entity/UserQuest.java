package com.nameless.social.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;

import java.time.LocalDateTime;

@Setter
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
public class UserQuest extends BaseTimeEntity {
	@EmbeddedId
	private UserQuestId id;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("userId")
	private User user;

	@ManyToOne(fetch = FetchType.LAZY)
	@MapsId("questId")
	private Quest quest;

	@Column(name = "is_success", nullable = false)
	private boolean isSuccess = false;

	@Column(name = "is_visible", nullable = false)
	private boolean isVisible = true;

	@Column(nullable = false)
	@CreatedDate
	private LocalDateTime assigned_at;

	private LocalDateTime success_at;

	public UserQuest(User user, Quest quest) {
		this.id = new UserQuestId(user.getId(), quest.getId());
		this.user = user;
		this.quest = quest;
		this.isSuccess = false;
	}
}

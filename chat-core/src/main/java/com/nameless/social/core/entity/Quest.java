package com.nameless.social.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Quest extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long id;

	@Column(nullable = false)
	private String name;

	private String description;

	private String tag;

	@Setter
	@Column(nullable = false)
	private boolean isSuccess;

	@ManyToOne
	@JoinColumn(name = "club_id")
	private Club club;

	@ManyToOne
	@JoinColumn(name = "user_id")
	private User user;

	// test code에서만 사용
	public Quest(final long id, final String name, final boolean isSuccess) {
		this.id = id;
		this.name = name;
		this.isSuccess = isSuccess;
	}
}

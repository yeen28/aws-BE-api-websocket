package com.nameless.social.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class Club extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "group_id")
	private Group group;

	@OneToMany(mappedBy = "club")
	private List<UserClub> participants = new ArrayList<>();

//	@OneToMany(mappedBy = "club")
//	private List<ChatMessage> messages = new ArrayList<>();

	public Club(String name) {
		this.name = name;
	}
}

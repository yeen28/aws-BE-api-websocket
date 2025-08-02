package com.nameless.social.core.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "users")
public class User extends BaseTimeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String token; // AWS Cognito User-sub

	@Column(nullable = false)
	private String username;

	@Setter
	@Transient
	private List<ChatRoomUser> chatRooms = new ArrayList<>();

	public User(String token, String username) {
		this.token = token;
		this.username = username;
	}
}

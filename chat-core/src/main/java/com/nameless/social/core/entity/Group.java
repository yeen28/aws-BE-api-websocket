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
@Table(name = "`group`")
public class Group extends BaseTimeEntity { // Category
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String name;

	@Column
	private String description;

	@OneToMany(mappedBy = "group")
	private List<UserGroup> userGroups = new ArrayList<>();

	@OneToMany(mappedBy = "group")
	private List<Club> clubs = new ArrayList<>();

	public Group(String name) {
		this.name = name;
	}
}

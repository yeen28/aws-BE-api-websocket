package com.nameless.social.api.model;

import com.nameless.social.core.entity.Group;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class GroupInfoModel {
	private String name;
	private String description;
	private String icon;
	private long memberNum;
	private List<String> questList;
	private List<Long> questSuccessNum;
	private List<String> tag;
	private List<ClubGroupInfoModel> clubList;
	private LocalDate questCreateTime;

	public static GroupInfoModel of(Group group) {
		return GroupInfoModel.builder()
				.name(group.getName())
				.description(group.getDescription())
				.build();
	}
}

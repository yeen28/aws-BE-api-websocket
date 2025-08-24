package com.nameless.social.api.model;

import com.nameless.social.core.entity.Club;
import com.nameless.social.core.entity.Group;
import com.nameless.social.core.entity.Quest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@Builder
public class GroupInfoModel {
	private long groupId;
	private String name; // group name
	private String description;
	private String icon;
	private long memberNum; // 그룹에 가입한 user 수
	private List<String> questList; // ???
	private List<Long> questSuccessNum;
	private List<String> tag;
	private List<ClubGroupInfoModel> clubList;
	private LocalDate questCreateTime;

	public static GroupInfoModel of(
			final Group group,
			final List<Club> clubs,
			final long clubMemberNum,
			final List<Quest> quests,
			final List<String> tags,
			final List<Long> questSuccessNum
	) {
		List<ClubGroupInfoModel> clubGroupInfoModels = clubs.stream()
				.map(club -> ClubGroupInfoModel.builder()
						.clubId(club.getId())
						.name(club.getName())
						.description(club.getDescription())
						.icon(club.getIcon())
						.memberNum(clubMemberNum)
						.build()
				)
				.toList();

		List<String> questNames = quests.stream()
				.map(Quest::getName)
				.toList();

		return GroupInfoModel.builder()
				.groupId(group.getId())
				.name(group.getName())
				.description(group.getDescription())
				.icon(group.getIcon())
				.memberNum(clubMemberNum)
				.questList(questNames)
				.questSuccessNum(questSuccessNum)
				.tag(tags)
				.clubList(clubGroupInfoModels)
//				.questCreateTime()
				.build();
	}
}

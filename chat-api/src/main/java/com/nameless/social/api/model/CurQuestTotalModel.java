package com.nameless.social.api.model;

import com.nameless.social.core.entity.Club;
import com.nameless.social.core.entity.Group;
import com.nameless.social.core.entity.Quest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;
import java.util.Map;

import static com.nameless.social.api.utils.StrUtils.parseTags;

@Getter
@Setter
@Builder
public class CurQuestTotalModel {
	private long questId;
	private String quest; // quest name
	private String group; // group name
	private String club; // club name
	private boolean isSuccess;
	private List<String> tag;
	private String description;

	public static CurQuestTotalModel of(final Quest quest, Map<Group, Club> groupClubMap) {
		Group group = groupClubMap.keySet().iterator().next();
		Club club = groupClubMap.values().iterator().next();

		return CurQuestTotalModel.builder()
				.questId(quest.getId())
				.quest(quest.getName())
				.group(group.getName())
				.club(club.getName())
				.isSuccess(quest.isSuccess())
				.tag(parseTags(quest.getTag()))
				.description(quest.getDescription())
				.build();
	}
}

package com.nameless.social.api.model;

import com.nameless.social.api.utils.StrUtils;
import com.nameless.social.core.entity.Quest;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.List;

@Getter
@Setter
@Builder
public class AddQuestModel {
	private long id; // quest id
	private String name;
	private String description;
	private List<String> tag;
	private long clubId;
	private LocalDateTime createdAt;
	private LocalDateTime updatedAt;

	public static AddQuestModel of(final Quest quest) {
		return AddQuestModel.builder()
				.id(quest.getId())
				.name(quest.getName())
				.description(quest.getDescription())
				.tag(StrUtils.parseTags(quest.getTag()))
				.clubId(quest.getClub().getId())
				.createdAt(quest.getCreatedAt())
				.updatedAt(quest.getUpdatedAt())
				.build();
	}
}

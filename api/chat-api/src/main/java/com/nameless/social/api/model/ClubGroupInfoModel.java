package com.nameless.social.api.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class ClubGroupInfoModel {
	private long clubId;
	private String name;
	private String description;
	private String icon;
	private long memberNum;
}

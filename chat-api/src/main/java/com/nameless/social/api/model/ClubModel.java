package com.nameless.social.api.model;

import lombok.Getter;
import lombok.Setter;
import java.util.List;

@Getter
@Setter
public class ClubModel {
	private String name;
	private String description;
	private String icon;
	private long memberNum; // TODO 중복데이터 -> 제거해도 되는지 확인 필요
	private List<String> tag;
}

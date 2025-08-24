package com.nameless.social.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class JoinGroupDto {
	private String user; // TODO 이전 사용자의 정보를 넘기는 것 같은데 user id를 전달하거나 token으로 확인해도 될듯..?
	private String group;
}

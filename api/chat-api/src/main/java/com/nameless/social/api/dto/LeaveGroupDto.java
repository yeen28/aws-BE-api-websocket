package com.nameless.social.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class LeaveGroupDto {
	private String user;
	private String group;
}

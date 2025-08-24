package com.nameless.social.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
@Builder
public class JoinClubDto {
	private String user;
	private String group;
	private List<Long> clubIds;
}

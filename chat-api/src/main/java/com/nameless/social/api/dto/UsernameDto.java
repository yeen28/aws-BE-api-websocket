package com.nameless.social.api.dto;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class UsernameDto {
	private String user;
	private String username;
}

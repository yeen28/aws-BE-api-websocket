package com.nameless.social.api.dto;

import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class ChatRoomDto {
	private String name;
	private List<Long> participantIds;
}

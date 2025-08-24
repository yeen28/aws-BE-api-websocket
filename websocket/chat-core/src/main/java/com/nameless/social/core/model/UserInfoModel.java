package com.nameless.social.core.model;

import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class UserInfoModel {
    private long id;
	private String email;
	private String name;
}

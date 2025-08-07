package com.nameless.social.core.entity;

import jakarta.persistence.Embeddable;

import java.io.Serializable;
import java.util.Objects;

@Embeddable
public class UserClubId implements Serializable {
	private Long userId;
	private Long clubId;

	// equals()와 hashCode() 반드시 override 필요!
	// @EmbeddedId, @IdClass를 사용하는 경우 키 비교를 정확히 하려면 필수입니다.
	@Override
	public boolean equals(Object o) {
		if (this == o) {
			return true;
		}
		if (o == null || getClass() != o.getClass()) {
			return false;
		}

		UserClubId that = (UserClubId) o;

		return Objects.equals(userId, that.userId) &&
				Objects.equals(clubId, that.clubId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, clubId);
	}
}

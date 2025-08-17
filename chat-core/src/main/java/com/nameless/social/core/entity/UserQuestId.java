package com.nameless.social.core.entity;

import jakarta.persistence.Embeddable;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.io.Serializable;
import java.util.Objects;

@Setter
@Getter
@AllArgsConstructor
@NoArgsConstructor
@Embeddable
public class UserQuestId implements Serializable {
	private long userId;
	private long questId;

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

		UserQuestId that = (UserQuestId) o;

		return Objects.equals(userId, that.userId) &&
				Objects.equals(questId, that.questId);
	}

	@Override
	public int hashCode() {
		return Objects.hash(userId, questId);
	}
}

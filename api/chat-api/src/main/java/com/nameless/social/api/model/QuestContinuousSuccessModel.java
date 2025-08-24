package com.nameless.social.api.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuestContinuousSuccessModel {
	private long days; // 전날 기준으로 퀘스트를 연속으로 성공한 날짜 카운팅
	private long totalQuestNum; // 30일동안 사용자가 받은 퀘스트 수
	private long successQuestNum; // 30일 동안 사용자가 성공한 퀘스트 수
}

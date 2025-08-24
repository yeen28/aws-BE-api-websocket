package com.nameless.social.api.model;

import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@Builder
public class QuestWeeklyModel {
	private long day; // 일~토 요일을 숫자로 반환
	private long questTotalNum; // 한 주마다 사용자가 가지고 있는 퀘스트 갯수
	private long successQuestNum; // 한 주마다 사용자가 클리어한 퀘스트 갯수
	private String bestParticipateGroup; // ??? 각 요일별로 가장 활발하게 퀘스트를 클리어한 그룹
}

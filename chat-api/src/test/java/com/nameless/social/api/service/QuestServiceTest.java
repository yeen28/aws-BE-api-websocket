package com.nameless.social.api.service;

import com.nameless.social.api.repository.ClubRepository;
import com.nameless.social.api.repository.GroupRepository;
import com.nameless.social.api.repository.QuestRepository;
import com.nameless.social.api.repository.UserGroupRepository;
import com.nameless.social.api.repository.user.UserRepository;
import com.nameless.social.core.entity.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class QuestServiceTest {
	@InjectMocks
	private QuestService questService;

	@Mock
	private UserRepository userRepository;
	@Mock
	private GroupRepository groupRepository;
	@Mock
	private UserGroupRepository userGroupRepository;
	@Mock
	private ClubRepository clubRepository;
	@Mock
	private QuestRepository questRepository;

	private User user;
	private Group group;
	private Club club;
	private Quest quest;
	private UserGroup userGroup;

	@BeforeEach
	void setUp() {
		user = new User("test token", "test", "test@test.com");
		group = new Group(1L, "test Group", "[\"절약\", \"투자\", \"예산관리\", \"자동화\"]");
		club = new Club(1L, "ClubA");
		quest = new Quest(1L, "test Quest", false);
		userGroup = new UserGroup(user, group);
	}

	@Test
	@DisplayName("퀘스트 조회")
	void getQuest() {
		// given
//		given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));
//		given(userGroupRepository.findAllByIdUserId(user.getId())).willReturn(List.of(userGroup));

		// when

		// then
	}
}
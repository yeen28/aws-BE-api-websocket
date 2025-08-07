package com.nameless.social.api.service;

import com.nameless.social.api.model.GroupInfoModel;
import com.nameless.social.api.model.GroupModel;
import com.nameless.social.api.repository.GroupRepository;
import com.nameless.social.api.repository.user.UserRepository;
import com.nameless.social.core.entity.Group;
import com.nameless.social.core.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class GroupServiceTest {
	@InjectMocks
	private GroupService groupService;

	@Mock
	private UserRepository userRepository;

	@Mock
	private GroupRepository groupRepository;

	private User user;
	private Group group;

	@BeforeEach
	void setUp() {
		user = new User("test token", "test", "test@test.com");
		group = new Group("test Group");
	}

	@Test
	@DisplayName("유저 이메일로 그룹 리스트 조회")
	void getGroupListByUserEmailTest() {
		// given
		given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

		// when
		List<GroupModel> groupList = groupService.getGroupListByUserEmail(user.getEmail());

		// then
		assertThat(groupList).isNotNull();
	}

	@Test
	@DisplayName("그룹 정보 조회")
	void getGroupInfoTest() {
		// given
		given(groupRepository.findByName(group.getName())).willReturn(Optional.of(group));

		// when
		GroupInfoModel groupInfo = groupService.getGroupInfo(group.getName());

		// then
		assertThat(groupInfo.getName()).isEqualTo("test Group");
	}

	@Test
	@DisplayName("그룹 목록 조회")
	void getGroupListTest() {
		// given
		given(groupRepository.findAll()).willReturn(List.of(group));

		// when
		List<GroupInfoModel> groupList = groupService.getGroupList();

		// then
		assertThat(groupList).isNotNull();
	}
}

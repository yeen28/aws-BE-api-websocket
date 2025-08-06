package com.nameless.social.api.service;

import com.nameless.social.api.model.GroupInfoModel;
import com.nameless.social.api.model.GroupModel;
import com.nameless.social.api.repository.chat.ClubRepository;
import com.nameless.social.api.repository.user.UserRepository;
import com.nameless.social.core.entity.Club;
import com.nameless.social.core.entity.ClubUser;
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
	private ClubRepository groupRepository;

	private User user;
	private Club chatRoom;

	@BeforeEach
	void setUp() {
		user = new User("test token", "test", "test@test.com");
		chatRoom = new Club("test chat room");
		ClubUser chatRoomUser = new ClubUser(user, chatRoom);
		user.getChatRooms().add(chatRoomUser);
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
		given(groupRepository.findByName(chatRoom.getName())).willReturn(Optional.of(chatRoom));

		// when
		GroupInfoModel groupInfo = groupService.getGroupInfo(chatRoom.getName());

		// then
		assertThat(groupInfo.getName()).isEqualTo("test chat room");
	}
}

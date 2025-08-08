package com.nameless.social.api.service;

import com.nameless.social.api.exception.CustomException;
import com.nameless.social.api.exception.ErrorCode;
import com.nameless.social.api.model.user.UserModel;
import com.nameless.social.api.repository.UserClubRepository;
import com.nameless.social.api.repository.UserGroupRepository;
import com.nameless.social.api.repository.user.UserRepository;
import com.nameless.social.core.entity.Club;
import com.nameless.social.core.entity.Group;
import com.nameless.social.core.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.anyLong;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

@ExtendWith(MockitoExtension.class)
class UserServiceTest {
	@InjectMocks
	private UserService userService;
	@Mock
	private UserRepository userRepository;
	@Mock
	private UserClubRepository userClubRepository;
	@Mock
	private UserGroupRepository userGroupRepository;

	private User user;
	private Group group;
	private Club club;

	@BeforeEach
	void setUp() {
		user = new User("test token", "test", "test@test.com");
		group = new Group("test Group");
		club = new Club("test Club");
	}

	@Test
	@DisplayName("유저 이메일 조회 성공")
	void getUserInfoTest() {
		// given
		final String email = "test@test.com";
		final User user = new User("test", "test", email);
		given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

		// when
		UserModel userModel = userService.getUserInfo(email);

		// then
		assertThat(userModel.getId()).isEqualTo(email);
	}

	@Test
	@DisplayName("유저 이메일 조회 실패 - 존재하지 않는 유저")
	void getUserInfoTest_fail_not_exist() {
		// given
		final String email = "test@test.com";
		given(userRepository.findByEmail(email)).willReturn(Optional.empty());

		// when
		// then
		CustomException exception = assertThrows(CustomException.class, () -> {
			userService.getUserInfo(email);
		});
		assertThat(exception.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
	}

	@Test
	@DisplayName("user 탈퇴")
	void deleteUserTest() {
		// given
		final String email = "test@test.com";
		given(userRepository.findByEmail(email)).willReturn(Optional.of(user));

		// when
		userService.deleteUser(email);

		// then
		verify(userClubRepository, times(1)).deleteByUserId(anyLong());
		verify(userGroupRepository, times(1)).deleteByUserId(anyLong());
		verify(userRepository, times(1)).deleteById(anyLong());
	}
}

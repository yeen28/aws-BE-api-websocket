package com.nameless.social.api.service;

import com.nameless.social.api.model.ClubModel;
import com.nameless.social.api.repository.ClubRepository;
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

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.BDDMockito.given;

@ExtendWith(MockitoExtension.class)
class ClubServiceTest {
	@InjectMocks
	private ClubService clubService;

	@Mock
	private ClubRepository clubRepository;

	private User user;
	private Club club;

	@BeforeEach
	void setUp() {
		user = new User("test token", "test", "test@test.com");
		club = new Club("test Club");
		ClubUser clubUser = new ClubUser(user, club);
		user.getClubs().add(clubUser);
	}

	@Test
	@DisplayName("유저 이메일로 그룹 리스트 조회")
	void getGroupListByUserEmailTest() {
		// given
		List<Club> clubs = List.of(club);
		given(clubRepository.findAll()).willReturn(List.of(club));

		// when
		List<ClubModel> clubList = clubService.findAll();

		// then
		assertThat(clubList).isNotNull();
	}
}

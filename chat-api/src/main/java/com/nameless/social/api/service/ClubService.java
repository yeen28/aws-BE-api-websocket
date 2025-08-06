package com.nameless.social.api.service;

import com.nameless.social.api.dto.ClubDto;
import com.nameless.social.api.exception.CustomException;
import com.nameless.social.api.exception.ErrorCode;
import com.nameless.social.api.model.chat.ClubModel;
import com.nameless.social.api.repository.chat.ClubRepository;
import com.nameless.social.api.repository.chat.ClubUserRepository;
import com.nameless.social.api.repository.user.UserRepository;
import com.nameless.social.core.entity.ClubUser;
import com.nameless.social.core.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class ClubService {
	private final ClubRepository clubRepository;
	private final UserRepository userRepository;
	private final ClubUserRepository chatRoomUserRepository;

	@Transactional
	public ClubModel createClub(final ClubDto chatRoomDto) {
		com.nameless.social.core.entity.Club chatRoom = new com.nameless.social.core.entity.Club(chatRoomDto.getName());
		clubRepository.save(chatRoom);

		List<User> participants = userRepository.findAllById(chatRoomDto.getParticipantIds());
		for (User user : participants) {
			ClubUser chatRoomUser = new ClubUser(user, chatRoom);
			chatRoomUserRepository.save(chatRoomUser);
		}

		return ClubModel.of(chatRoom);
	}

	public ClubModel findById(final Long id) {
		return ClubModel.of(clubRepository.findById(id)
				.orElseThrow(() -> new CustomException(ErrorCode.CLUB_NOT_FOUND)));
	}

	public List<ClubModel> findAll() {
		return clubRepository.findAll().stream()
				.map(ClubModel::of)
				.collect(Collectors.toList());
	}
}

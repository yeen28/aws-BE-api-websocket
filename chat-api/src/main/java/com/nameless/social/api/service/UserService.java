package com.nameless.social.api.service;

import com.nameless.social.api.dto.UserDto;
import com.nameless.social.api.exception.CustomException;
import com.nameless.social.api.exception.ErrorCode;
import com.nameless.social.api.model.user.*;
import com.nameless.social.api.repository.user.UserRepository;
import com.nameless.social.core.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.Optional;

@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class UserService {
	private final UserRepository userRepository;

	@Transactional
	public UserModel getOrCreateUser(final UserDto userDto) {
		Optional<User> userOptional = userRepository.findByToken(userDto.getToken());
		return UserModel.of(
				userOptional.orElseGet(() ->
						userRepository.save(new User(userDto.getToken(), userDto.getName(), userDto.getEmail()))
				)
		);
	}

	public UserModel findById(Long id) {
		return UserModel.of(userRepository.findById(id)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND))
		);
	}

	public void getUserQuestContinuous(final String email) {
	}

	public UserModel getUserInfo(final String email) {
		// TODO User 유효성 검증
		return UserModel.of(userRepository.findByEmail(email)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND))
		);
	}
}

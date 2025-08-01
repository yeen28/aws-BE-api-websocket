package com.nameless.social.api.service;

import com.nameless.social.api.dto.UserDto;
import com.nameless.social.api.exception.CustomException;
import com.nameless.social.api.exception.ErrorCode;
import com.nameless.social.api.model.user.UserModel;
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
		Optional<User> userOptional = userRepository.findBySocialId(userDto.getSocialId());
		return UserModel.of(
				userOptional.orElseGet(() ->
						userRepository.save(new User(userDto.getSocialId(), userDto.getUsername()))
				)
		);
	}

	public UserModel findById(Long id) {
		return UserModel.of(userRepository.findById(id)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND))
		);
	}
}

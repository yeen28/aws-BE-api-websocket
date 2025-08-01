package com.nameless.social.api.service;

import com.nameless.social.api.exception.CustomException;
import com.nameless.social.api.exception.ErrorCode;
import com.nameless.social.api.repository.UserRepository;
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
	public User getOrCreateUser(String socialId, String username) {
		Optional<User> userOptional = userRepository.findBySocialId(socialId);
		return userOptional.orElseGet(() -> userRepository.save(new User(socialId, username)));
	}

	public User findById(Long id) {
		return userRepository.findById(id)
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	}
}

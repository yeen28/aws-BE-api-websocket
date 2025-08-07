package com.nameless.social.api.handler;

import com.nameless.social.api.repository.user.UserRepository;
import com.nameless.social.core.entity.User;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Component
public class UserInfoArgumentResolver implements HandlerMethodArgumentResolver {
	private final UserRepository userRepository;

	public UserInfoArgumentResolver(UserRepository userRepository) {
		this.userRepository = userRepository;
	}

	@Override
	public boolean supportsParameter(MethodParameter parameter) {
		return parameter.hasParameterAnnotation(UserInfo.class)
				&& parameter.getParameterType().equals(User.class);
	}

	@Override
	public Object resolveArgument(
			MethodParameter parameter,
			ModelAndViewContainer mavContainer,
			NativeWebRequest webRequest,
			WebDataBinderFactory binderFactory
	) {
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();

		if (authentication == null || !authentication.isAuthenticated()) {
			return null; // or throw exception
		}

		String userToken = authentication.getName(); // or authentication.getPrincipal()

		return userRepository.findByToken(userToken)
				.orElseThrow(() -> new IllegalArgumentException("User not found"));
	}
}
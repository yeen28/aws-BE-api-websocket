package com.nameless.social.api.handler;

import com.nameless.social.api.auth.CognitoTokenVerifier;
import com.nameless.social.api.exception.CustomException;
import com.nameless.social.api.exception.ErrorCode;
import com.nameless.social.api.repository.user.UserRepository;
import com.nameless.social.core.entity.User;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.core.MethodParameter;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.bind.support.WebDataBinderFactory;
import org.springframework.web.context.request.NativeWebRequest;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.method.support.ModelAndViewContainer;

@Slf4j
@Component
public class UserInfoArgumentResolver implements HandlerMethodArgumentResolver {
	private final UserRepository userRepository;
	private final CognitoTokenVerifier cognitoTokenVerifier;

	public UserInfoArgumentResolver(UserRepository userRepository, CognitoTokenVerifier cognitoTokenVerifier) {
		this.userRepository = userRepository;
		this.cognitoTokenVerifier = cognitoTokenVerifier;
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
		// Authorization 헤더 가져오기
		String authHeader = webRequest.getHeader("Authorization");
		if (StringUtils.isEmpty(authHeader) || !authHeader.startsWith("Bearer ")) {
			// 인증 토큰이 없거나 형식이 맞지 않음
			return null; // 또는 예외 처리
		}
		// "Bearer " 접두어 제거
		String token = authHeader.substring(7);

		// 토큰 검증 (예: CognitoTokenVerifier 사용)
//		boolean verify = cognitoTokenVerifier.verify(token);
//		if (!verify) {
//			throw new CustomException(ErrorCode.INVALID_TOKEN);
//		}

		String email = cognitoTokenVerifier.extractEmailFromToken(token);

		// 이후 인증 객체에서 사용자 정보 확인 가능
		Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
		if (authentication == null || !authentication.isAuthenticated()) {
			log.warn("인증 객체에서 사용자 정보 확인 불가능");
			return null; // or throw exception
		}

		// TODO 사용자의 권한도 체크해야 함. ex) 관리자인지 일반 사용자인지 등등...
//		String userToken = authentication.getName(); // or authentication.getPrincipal()
//
//		Object principal = authentication.getPrincipal();
//
//		String email;
//		if (principal instanceof UserDetails) {
//			email = ((UserDetails) principal).getUsername(); // 일반적으로 username이 email인 경우 많음
//		} else if (principal instanceof String) {
//			email = (String) principal; // 직접 문자열인 경우
//		} else {
//			// 커스텀 타입인 경우, 해당 타입에서 이메일 꺼내기
//			// email = ((CustomUserDetails)principal).getEmail();
//			email = null; // 일단 null 처리
//		}

		return userRepository.findByEmail("admin@nameless.com")
				.orElseThrow(() -> new CustomException(ErrorCode.USER_NOT_FOUND));
	}
}
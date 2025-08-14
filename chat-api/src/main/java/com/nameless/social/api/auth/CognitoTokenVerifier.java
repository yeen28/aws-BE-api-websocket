package com.nameless.social.api.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import io.micrometer.common.util.StringUtils;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import software.amazon.awssdk.regions.Region;
import software.amazon.awssdk.services.cognitoidentityprovider.CognitoIdentityProviderClient;
import software.amazon.awssdk.services.cognitoidentityprovider.model.*;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Slf4j
@Component
public class CognitoTokenVerifier {
	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String issuer;
	@Value("${spring.security.oauth2.resourceserver.client-id}")
	private String clientId;

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

	/**
	 * Cognito로 전달받은 JWT 토큰 검증
	 * @param token
	 * @return true 인증 성공 | faLse 인증 실패
	 */
	public boolean verify(String token) {
		try {
			JWKSet jwkSet = JWKSet.load(new URL(issuer + "/.well-known/jwks.json"));
			SignedJWT signedJWT = SignedJWT.parse(token);
			JWK jwk = jwkSet.getKeyByKeyId(signedJWT.getHeader().getKeyID());

			JWSVerifier verifier = new RSASSAVerifier(jwk.toRSAKey());
			if (!signedJWT.verify(verifier)) {
				return false;
			}

			JWTClaimsSet claims = signedJWT.getJWTClaimsSet();
			if (!issuer.equals(claims.getIssuer())) {
				return false;
			}

			if (!claims.getAudience().contains(clientId)) {
				log.warn("Unauthorized User - {}", claims.getAudience().isEmpty() ? "" : claims.getAudience().get(0));
				return false;
			}

			return !new Date().after(claims.getExpirationTime());

		} catch (Exception e) {
			log.warn(e.getMessage());
			return false;
		}
	}

	/**
	 * idToken을 받아서 email을 추출
	 * @param token idToken
	 * @return
	 */
	public String extractEmailFromToken(String token) {
		try {
			// JWT 페이로드 부분 디코딩
			String[] parts = token.split("\\.");
			if (parts.length < 2) {
				return null;
			}
			String payloadJson = new String(Base64.getUrlDecoder().decode(parts[1]), StandardCharsets.UTF_8);

			// JSON에서 email 필드 추출 (Jackson 등 JSON 파서 사용)
			JsonNode node = OBJECT_MAPPER.readTree(payloadJson);

			// email이 없으면 username도 시도
			if (node.has("email")) {
				return node.get("email").asText();
			} else if (node.has("username")) {
				return node.get("username").asText();
			} else {
				return "";
			}
		} catch (Exception e) {
			log.warn(e.getMessage());
			return "";
		}
	}


	/**
	 * 비밀번호 강제 변경 & 토큰 얻기
	 * @param clientId
	 * @param username
	 * @param oldPassword
	 * @param newPassword
	 */
	private void changePwdAndGetToken(
			final String clientId,
			final String username,
			final String oldPassword,
			final String newPassword
	) {
		if (StringUtils.isBlank(clientId)
				|| StringUtils.isBlank(username)
				|| StringUtils.isBlank(oldPassword)
				|| StringUtils.isBlank(newPassword)
		) {
			return;
		}

		CognitoIdentityProviderClient client = CognitoIdentityProviderClient.builder()
				.region(Region.AP_NORTHEAST_2)
				.build();

		// 1. initiate-auth 호출
		InitiateAuthRequest initRequest = InitiateAuthRequest.builder()
				.authFlow(AuthFlowType.USER_PASSWORD_AUTH)
				.clientId(clientId)
				.authParameters(Map.of(
						"USERNAME", username,
						"PASSWORD", oldPassword
				))
				.build();

		InitiateAuthResponse initResponse = client.initiateAuth(initRequest);

		if ("NEW_PASSWORD_REQUIRED".equals(initResponse.challengeNameAsString())) {
			System.out.println("NEW_PASSWORD_REQUIRED 챌린지 발생");

			// 2. respond-to-auth-challenge 호출
			Map<String, String> challengeResponses = new HashMap<>();
			challengeResponses.put("USERNAME", username);
			challengeResponses.put("NEW_PASSWORD", newPassword);

			RespondToAuthChallengeRequest respondRequest = RespondToAuthChallengeRequest.builder()
					.challengeName(ChallengeNameType.NEW_PASSWORD_REQUIRED)
					.clientId(clientId)
					.challengeResponses(challengeResponses)
					.session(initResponse.session()) // 필수: 이전 호출에서 받은 세션
					.build();

			RespondToAuthChallengeResponse respondResponse = client.respondToAuthChallenge(respondRequest);

//			System.out.println("Token: " + respondResponse.authenticationResult());
//			System.out.println("Access Token: " + respondResponse.authenticationResult().accessToken());
			System.out.println("Id Token: " + respondResponse.authenticationResult().idToken());
//			System.out.println("Refresh Token: " + respondResponse.authenticationResult().refreshToken());
		} else {
			// NEW_PASSWORD_REQUIRED가 아닌 경우 정상 로그인
			System.out.println("Token: " + initResponse.authenticationResult());
			System.out.println("Access Token: " + initResponse.authenticationResult().accessToken());
		}
	}
}
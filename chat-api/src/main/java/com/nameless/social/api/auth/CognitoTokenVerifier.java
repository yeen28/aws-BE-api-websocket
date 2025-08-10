package com.nameless.social.api.auth;

import com.fasterxml.jackson.databind.JsonNode;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.nimbusds.jose.JWSVerifier;
import com.nimbusds.jose.crypto.RSASSAVerifier;
import com.nimbusds.jose.jwk.JWK;
import com.nimbusds.jose.jwk.JWKSet;
import com.nimbusds.jwt.JWTClaimsSet;
import com.nimbusds.jwt.SignedJWT;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Base64;
import java.util.Date;

@Slf4j
@Component
public class CognitoTokenVerifier {
	@Value("${spring.security.oauth2.resourceserver.jwt.issuer-uri}")
	private String issuer;
	@Value("${spring.security.oauth2.resourceserver.client-id}")
	private String clientId;

	private static final ObjectMapper OBJECT_MAPPER = new ObjectMapper();

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
}
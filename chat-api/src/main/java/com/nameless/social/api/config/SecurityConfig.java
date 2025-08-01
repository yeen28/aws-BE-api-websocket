package com.nameless.social.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.web.SecurityFilterChain;

@Configuration
@EnableWebSecurity
public class SecurityConfig {
	@Bean
	public SecurityFilterChain securityFilterChain(HttpSecurity http) throws Exception {
		http
				.csrf(AbstractHttpConfigurer::disable)
				.authorizeHttpRequests(authorize -> authorize
//						.requestMatchers("/api/**").authenticated()
								.anyRequest().permitAll()
				);
//				.oauth2ResourceServer(oauth2 -> oauth2.jwt()); // JWT 기반 리소스 서버 설정
		return http.build();
	}
}

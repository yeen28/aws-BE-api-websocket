package com.nameless.social.api.handler;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	private final UserInfoArgumentResolver userInfoArgumentResolver;

	public WebConfig(UserInfoArgumentResolver userInfoArgumentResolver) {
		this.userInfoArgumentResolver = userInfoArgumentResolver;
	}

	@Bean
	public RestTemplate restTemplate() {
		return new RestTemplate();
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(userInfoArgumentResolver);
	}
}

package com.nameless.social.api.handler;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.method.support.HandlerMethodArgumentResolver;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

import java.util.List;

@Configuration
public class WebConfig implements WebMvcConfigurer {
	private final UserInfoArgumentResolver userInfoArgumentResolver;

	public WebConfig(UserInfoArgumentResolver userInfoArgumentResolver) {
		this.userInfoArgumentResolver = userInfoArgumentResolver;
	}

	@Override
	public void addArgumentResolvers(List<HandlerMethodArgumentResolver> resolvers) {
		resolvers.add(userInfoArgumentResolver);
	}

	@Override
	public void addCorsMappings(CorsRegistry registry) {
		registry.addMapping("/**")
				.allowedOrigins(
						"http://localhost:4200",
						"https://server.teamnameless.click",
						"https://stage.teamnameless.click",
						"https://www.teamnameless.click",
						"https://teamnameless.click"
				)
				.allowedMethods("*")
				.allowedHeaders("*")
				.allowCredentials(true);
	}
}

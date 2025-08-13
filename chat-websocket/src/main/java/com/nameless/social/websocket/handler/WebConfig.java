package com.nameless.social.websocket.handler;

import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {
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

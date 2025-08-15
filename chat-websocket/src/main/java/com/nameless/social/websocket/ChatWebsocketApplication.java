package com.nameless.social.websocket;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.WebApplicationType;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.boot.autoconfigure.orm.jpa.HibernateJpaAutoConfiguration;

@SpringBootApplication
@EnableAutoConfiguration(exclude = {DataSourceAutoConfiguration.class, HibernateJpaAutoConfiguration.class})
public class ChatWebsocketApplication {
	public static void main(String[] args) {
		// WebFlux 의존성은 WebClient를 사용하는 데만 활용되고, 애플리케이션의 전체 동작은 안정적인 Spring MVC를 기반으로 하게 되어 SockJS 핸드셰이크 문제를 해결
		SpringApplication application = new SpringApplication(ChatWebsocketApplication.class);
		application.setWebApplicationType(WebApplicationType.SERVLET);
		application.run(args);
	}
}

package com.nameless.social.api;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.autoconfigure.jdbc.DataSourceAutoConfiguration;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.boot.autoconfigure.domain.EntityScan;

@EnableJpaAuditing
@SpringBootApplication(exclude = {DataSourceAutoConfiguration.class})
@EntityScan("com.nameless.social.core.entity")
public class ChatApiApplication {
	public static void main(String[] args) {
		SpringApplication.run(ChatApiApplication.class, args);
	}
}

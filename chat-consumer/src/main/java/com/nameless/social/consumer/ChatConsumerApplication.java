package com.nameless.social.consumer;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@EnableJpaAuditing
@SpringBootApplication
public class ChatConsumerApplication {
    public static void main(String[] args) {
        SpringApplication.run(ChatConsumerApplication.class, args);
    }
}

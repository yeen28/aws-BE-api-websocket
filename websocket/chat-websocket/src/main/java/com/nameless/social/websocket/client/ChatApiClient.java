package com.nameless.social.websocket.client;

import com.nameless.social.core.model.UserInfoModel;
import com.nameless.social.websocket.response.CommonResponse;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpStatusCode;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.ClientResponse;
import org.springframework.web.reactive.function.client.WebClient;
import org.springframework.web.reactive.function.client.WebClientResponseException;
import reactor.core.publisher.Mono;
import reactor.util.retry.Retry;

import java.time.Duration;
import java.util.concurrent.TimeoutException;

@Slf4j
@Component
public class ChatApiClient {
	private final WebClient webClient;

	public ChatApiClient(WebClient.Builder webClientBuilder, @Value("${services.chat-api.url}") String chatApiUrl) {
		this.webClient = webClientBuilder.baseUrl(chatApiUrl).build();
	}

	public Mono<Long> getUserIdByEmail(String email) {
		ParameterizedTypeReference<CommonResponse<UserInfoModel>> typeRef = new ParameterizedTypeReference<>() {};

		return webClient.get()
				.uri(uriBuilder -> uriBuilder.path("/api/user").queryParam("email", email).build())
				.retrieve()
				.onStatus(HttpStatusCode::is4xxClientError, this::handle4xxError)
				.bodyToMono(typeRef)
				.timeout(Duration.ofSeconds(5))
				.retryWhen(createRetryStrategy(email))
				.flatMap(response -> {
					if (response.isSuccess() && response.getData() != null) {
						return Mono.just(response.getData().getId());
					} else {
						return Mono.error(new RuntimeException("API call was not successful: " + response.getMessage()));
					}
				})
				.doOnError(error -> log.error("Error fetching user ID for email: {}", email, error));
	}

	private Mono<Throwable> handle4xxError(ClientResponse response) {
		return response.bodyToMono(String.class)
				.flatMap(errorBody -> {
					log.error("Client Error from chat-api: {} Body: {}", response.statusCode(), errorBody);
					return Mono.error(new WebClientResponseException(
							response.statusCode().value(), "Client Error", null, null, null));
				});
	}

	private Retry createRetryStrategy(String email) {
		return Retry.backoff(3, Duration.ofMillis(100))
				.filter(throwable -> throwable instanceof WebClientResponseException.ServiceUnavailable || throwable instanceof TimeoutException)
				.onRetryExhaustedThrow((retryBackoffSpec, retrySignal) -> {
					log.error("Retry exhausted for email: {}", email, retrySignal.failure());
					return retrySignal.failure();
				});
	}
}

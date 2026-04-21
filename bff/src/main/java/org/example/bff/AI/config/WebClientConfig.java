package org.example.bff.AI.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    WebClient.Builder webClientBuilder() {
        return WebClient.builder()
                .defaultHeader(HttpHeaders.ACCEPT, MediaType.APPLICATION_JSON_VALUE);
    }

    @Bean
    WebClient aiWebClient(
            WebClient.Builder builder,
            @Value("${ai.api.key}") String apiKey,
            @Value("${ai.api.baseUrl}") String baseUrl
    ) {
        if (apiKey == null || apiKey.isBlank()) {
            throw new IllegalArgumentException("ChatGPT API-key must be provided in application.properties");
        }
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalArgumentException("ChatGPT API baseUrl must be provided in application.properties");
        }

        return builder.clone()
                .baseUrl(baseUrl)
                .defaultHeader(HttpHeaders.AUTHORIZATION, "Bearer " + apiKey)
                .build();
    }

    @Bean
    WebClient externalWebClient(
            WebClient.Builder b,
            @Value("${external.api.baseUrl}") String baseUrl
    ) {
        if (baseUrl == null || baseUrl.isBlank()) {
            throw new IllegalArgumentException("External API baseUrl must be provided in application.properties");
        }

        return b.clone()
                .baseUrl(baseUrl)
                .build();
    }
}
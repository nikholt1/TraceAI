package org.example.bff.AI.client;
import org.example.bff.AI.service.ResponseDto;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;

@Service
public class AIClient {

    private final WebClient client;

    public AIClient(@Qualifier("aiWebClient") WebClient webClient) {
        this.client = webClient;
    }

    @Value("${ai.api.baseUrl}")
    private String baseUrl;

    @Value("${ai.api.key}")
    private String apiKey;

    // AIClient (BFF)
    public ResponseDto getResponses(String prompt) {
        Map<String, String> body = Map.of("prompt", prompt);

        return client.post()
                .uri(baseUrl + "/testWithoutCPU")
                .header("X-API-KEY", apiKey)
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .onStatus(s -> s.value() == 400,
                        r -> r.bodyToMono(String.class).map(msg -> new RuntimeException("400: " + msg)))
                .onStatus(s -> s.value() == 401,
                        r -> r.bodyToMono(String.class).map(msg -> new RuntimeException("401: " + msg)))
                .onStatus(HttpStatusCode::isError,
                        r -> r.bodyToMono(String.class).map(msg -> new RuntimeException("AI Error: " + msg)))
                .bodyToMono(ResponseDto.class)   // <-- change here
                .block();
    }

    public String establishConnection() {
        return client.get()
                .uri(baseUrl + "/establish")
                .header("X-API-KEY", apiKey)
                .retrieve()
                .bodyToMono(String.class)
                .block();
    }
}

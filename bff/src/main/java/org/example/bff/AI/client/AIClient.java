package org.example.bff.AI.client;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.MediaType;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;

import java.util.List;


@Service
public class AIClient {
    private final WebClient client;

    public AIClient(@Qualifier("aiWebClient") WebClient webClient) {
        this.client = webClient;
    }

    public record SimpleResponse(String id, List<Output> output) {}
    public record Output(String id, List<Content> content) {}
    public record Content(String text) {}

    public record SimpleRequest(String model, String input){}

    public Mono<SimpleResponse> getResponses(String prompt) {
        SimpleRequest body = new SimpleRequest("llama2", prompt);
        return client.post()
                .uri("/responses")
                .contentType(MediaType.APPLICATION_JSON)
                .bodyValue(body)
                .retrieve()
                .onStatus(s -> s.value() == 400, r -> r.bodyToMono(String.class)
                        .map(msg -> new IllegalArgumentException("OpenAI 400: " + msg)))
                .onStatus(s -> s.value() == 401, r -> r.bodyToMono(String.class)
                        .map(msg -> new IllegalArgumentException("OpenAI 401 Unauthorized: " + msg)))
                .onStatus(HttpStatusCode::isError, r -> r.bodyToMono(String.class)
                        .map(msg -> new IllegalArgumentException("OpenAI Error: " + msg)))
                .bodyToMono(SimpleResponse.class);
    }
}

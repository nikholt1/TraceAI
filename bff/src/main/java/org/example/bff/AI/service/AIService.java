package org.example.bff.AI.service;


import org.example.bff.AI.client.AIClient;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.Map;

@Service
public class AIService {

    private final AIClient aiClient;

    public AIService(AIClient aiClient) {
        this.aiClient = aiClient;
    }

    public record ResponseDto(String response) {}

    public Mono<ResponseDto> getResponse(String prompt) {
        return aiClient.getResponses(prompt)
                .map(this::mapToResponseDto);
    }

    private ResponseDto mapToResponseDto(AIClient.SimpleResponse resp) {
        StringBuilder combinedText = new StringBuilder();
        for (AIClient.Output output : resp.output()) {
            for (AIClient.Content content : output.content()) {
                combinedText.append(content.text());
            }
        }
        return new ResponseDto(combinedText.toString());
    }

}

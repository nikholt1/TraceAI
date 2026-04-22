package org.example.bff.AI.service;


import org.example.bff.AI.client.AIClient;
import org.example.bff.cisagovAPI.model.CISData;
import org.example.bff.cisagovAPI.service.CISDataService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Mono;

import java.util.List;
import java.util.Map;

@Service
public class AIService {

    private static final Logger log = LoggerFactory.getLogger(AIService.class);
    private final AIClient aiClient;
    private final CISDataService cisDataService;

    private final String INSTRUCTION = """
            If the user's message does not contain anything questioning the weather
            Rules:
            - Reprompt the user with the statement "i cannot answer anything else but about the weather"
                 
            """;



    public AIService(AIClient aiClient, CISDataService cisDataService) {
        this.aiClient = aiClient;
        this.cisDataService = cisDataService;
    }

    public record ResponseDto(String response) {}

    public Mono<ResponseDto> getResponse(String prompt) {
        cisDataService.createCISData();
        prompt = INSTRUCTION + prompt;
        prompt = checkAndInject(prompt);
        return aiClient.getResponses(prompt)
                .map(this::mapToResponseDto);
    }

    private String checkAndInject(String prompt) {
        String currentData = "";
        if (prompt.contains("weather")) {
            currentData = cisDataService.getNewestData();
        }
        prompt += currentData;
        System.out.println(prompt);
        return prompt;
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

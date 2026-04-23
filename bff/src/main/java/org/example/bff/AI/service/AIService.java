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

    private static final String INSTRUCTION = """
            If the user's message does not contain anything questioning the weather
            Rules:
            - Reprompt the user with the statement "i cannot answer anything else but about the weather"
            """;

    public AIService(AIClient aiClient, CISDataService cisDataService) {
        this.aiClient = aiClient;
        this.cisDataService = cisDataService;
    }

//    public record ResponseDto(String response) {}

    public ResponseDto getResponse(String prompt) {
        cisDataService.createCISData();

        String finalPrompt = checkAndInject(INSTRUCTION + prompt);

        ResponseDto dto = aiClient.getResponses(finalPrompt);

        if (dto == null || dto.response() == null || dto.response().isBlank()) {
            return new ResponseDto("No response from Ollama");
        }

        return dto; // <-- return as-is
    }

    private String checkAndInject(String prompt) {

        String currentData = "";

        if (prompt.contains("weather")) {
            currentData = cisDataService.getNewestData();
        }

        String result = prompt + currentData;

        System.out.println(result);

        return result;
    }
    public String establishConnection() {
        return aiClient.establishConnection();
    }
}

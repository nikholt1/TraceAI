package com.example.aiapi.ai.ollamaEngine;


import org.springframework.stereotype.Component;
import org.springframework.stereotype.Service;
import tools.jackson.core.type.TypeReference;
import tools.jackson.databind.ObjectMapper;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.Map;



@Service
public class EngineService {

    private final HttpClient client = HttpClient.newHttpClient();
    private final ObjectMapper mapper = new ObjectMapper();

    public String prompt(String promptText) {
        try {
            String body = """
                {
                  "model": "llama3.2:latest",
                  "prompt": "%s",
                  "stream": false
                }
                """.formatted(promptText.replace("\"", "\\\""));

            HttpRequest request = HttpRequest.newBuilder()
                    .uri(URI.create("http://localhost:11434/api/generate"))
                    .header("Content-Type", "application/json")
                    .POST(HttpRequest.BodyPublishers.ofString(body))
                    .build();

            HttpResponse<String> response =
                    client.send(request, HttpResponse.BodyHandlers.ofString());

            Map<String, Object> map = mapper.readValue(
                    response.body(),
                    new TypeReference<Map<String, Object>>() {}
            );

            return (String) map.get("response");

        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}

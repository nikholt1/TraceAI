package com.example.aiapi.ai.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class ResourceController {
    @GetMapping("/api/ollama/v1/establish")
    public String homeEndpoint() {
        return "How can i help you today?";
    }
}

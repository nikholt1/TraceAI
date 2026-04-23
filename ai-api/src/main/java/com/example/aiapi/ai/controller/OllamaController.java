package com.example.aiapi.ai.controller;


import com.example.aiapi.ai.service.OllamaService;
import com.example.aiapi.ai.service.ResponseDto;
import org.apache.coyote.Response;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("/api/ollama/v1")
public class OllamaController {
    private final OllamaService ollamaService;

    public OllamaController(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }

    public record PromptRequest(String prompt) {}
    @PostMapping
    public ResponseEntity<ResponseDto> prompt(@RequestBody PromptRequest request) {
        ResponseDto response = ollamaService.prompt(request.prompt());
        if (response.response().equals("401")) {
            return ResponseEntity.status(401).build();
        } else {
            return ResponseEntity.of(
                    Optional.of(response));
        }
    }

    @PostMapping("/testWithoutCPU")
    public ResponseEntity<ResponseDto> promptWithoutCPU(@RequestBody PromptRequest request) {
        if (request.prompt.equals("test fail")) {
            return ResponseEntity.status(401).build();
        } else {
            return ResponseEntity.ok(
                    new ResponseDto("I'd be happy to help you test the weather!")
            );

        }
    }

}

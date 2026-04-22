package com.example.aiapi.ai.controller;


import com.example.aiapi.ai.service.OllamaService;
import com.example.aiapi.ai.service.ResponseDto;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Optional;

@RestController
@RequestMapping("api/ollama/v1")
public class OllamaController {
    private final OllamaService ollamaService;

    public OllamaController(OllamaService ollamaService) {
        this.ollamaService = ollamaService;
    }


    @PostMapping()
    public ResponseEntity<ResponseDto> prompt(@RequestBody String prompt, @RequestBody String APIKey) {
        return ResponseEntity.of(Optional.of(ollamaService.prompt(prompt, APIKey)));
    }

}

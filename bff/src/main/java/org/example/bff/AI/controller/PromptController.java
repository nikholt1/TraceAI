package org.example.bff.AI.controller;


import org.example.bff.AI.service.AIService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
@RequestMapping("/api/prompt")
public class PromptController {

    private final AIService service;

    public PromptController(AIService service) {
        this.service = service;
    }

    public record QueryRequest(String prompt) {}

    @PostMapping
    public Mono<ResponseEntity<AIService.ResponseDto>> ask(@RequestBody QueryRequest queryRequest) {
        var response = service.getResponse(queryRequest.prompt);
        return response.map(ResponseEntity::ok);
    }

}

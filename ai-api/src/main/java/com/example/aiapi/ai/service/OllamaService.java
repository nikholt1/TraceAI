package com.example.aiapi.ai.service;


import com.example.aiapi.ai.model.APIKey;
import com.example.aiapi.ai.ollamaEngine.EngineService;
import com.example.aiapi.ai.repository.OllamaRepository;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OllamaService {
    private final OllamaRepository ollamaRepository;
    private final EngineService engineService;

    public OllamaService(OllamaRepository ollamaRepository, EngineService engineService) {
        this.ollamaRepository = ollamaRepository;
        this.engineService = engineService;
    }


    public ResponseDto prompt(String prompt) {

        String response = engineService.prompt(prompt);
        ResponseDto responseDto = new ResponseDto(response);
        return responseDto;
    }


}

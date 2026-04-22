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


    public ResponseDto prompt(String prompt, String APIKey) {
        if (isValid(APIKey)) {
            String response = engineService.prompt(prompt);
            ResponseDto responseDto = new ResponseDto(response);
            return responseDto;
        } else {
            return new ResponseDto("Unautrorized");
        }
    }

    public boolean isValid(String key) {
        if (key == null) {
            return false;
        }

        if (ollamaRepository.findByApiKey(key) != null) {
            return true;
        } else {
            return false;
        }
    }
}

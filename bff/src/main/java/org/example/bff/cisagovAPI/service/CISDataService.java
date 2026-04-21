package org.example.bff.cisagovAPI.service;


import org.example.bff.cisagovAPI.model.CISData;
import org.example.bff.cisagovAPI.repository.CISRepository;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;

@Service
public class CISDataService {

    private final WebClient webClient;
    private final CISRepository repo;

    public CISDataService(WebClient.Builder webClientBuilder, CISRepository repo) {
        this.webClient = webClientBuilder.build();
        this.repo = repo;
    }

    public CISData createCISData() {
        String apiData = webClient
                .get()
                .uri("${external.api.baseUrl}")
                .retrieve()
                .bodyToMono(String.class)
                .block();

        CISData entity = new CISData();
        entity.setFetchedAt(LocalDateTime.now());
        entity.setBody(apiData);

        return repo.save(entity);
    }
}

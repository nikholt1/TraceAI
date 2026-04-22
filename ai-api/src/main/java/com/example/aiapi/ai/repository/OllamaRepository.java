package com.example.aiapi.ai.repository;

import com.example.aiapi.ai.model.APIKey;
import com.example.aiapi.ai.service.ResponseDto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OllamaRepository extends JpaRepository<APIKey, Long> {


    String findByApiKey(String apiKey);
}

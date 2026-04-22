package com.example.aiapi.ai.config;



import com.example.aiapi.ai.model.APIKey;
import com.example.aiapi.ai.repository.OllamaRepository;
import org.springframework.boot.CommandLineRunner;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
@Configuration
public class InitApp {

    @Bean
    CommandLineRunner initDatabase(OllamaRepository ollamaRepository) {

        return args -> {
            APIKey apiKey = new APIKey();
            apiKey.setApiKey("thisisthekey32132321");

            ollamaRepository.save(apiKey);
        };
    }
}
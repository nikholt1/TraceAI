package org.example.bff.clients;


import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;

@Configuration
public class RestClientConfig {
    @Bean
    @Qualifier("resourceServerClient")
    RestClient resourceServerClient(RestClient.Builder builder) {
        return builder
                .baseUrl("http://localhost:8081") // URL of the resource server
                .build();
    }
}

package org.example.bff.cisagovAPI.service;


import org.example.bff.cisagovAPI.model.CISData;
import org.example.bff.cisagovAPI.repository.CISRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.reactive.function.client.WebClient;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Service
public class CISDataService {

    private final WebClient webClient;
    private final CISRepository repo;
    @Value("${external.api.baseUrl}")
    private String apiBaseUrl;
    public CISDataService(WebClient.Builder webClientBuilder, CISRepository repo) {
        this.webClient = webClientBuilder.build();
        this.repo = repo;
    }

    public CISData createCISData() {
        //hvis vi skal have specific data på enkelte variabler, bruger vi:

        // JsonNode currentWeather = json.get("current_weather");
        //
        // double temperature = currentWeather.get("temperature").asDouble();
        // double windspeed = currentWeather.get("windspeed").asDouble();

        // og sætter det bagefter:
        //     CISData entity = new CISData();
        //
        // entity.setFetchedAt(LocalDateTime.now());
        // entity.setTemperature(temperature);
        // entity.setWeather("Wind: " + windspeed + " km/h");

        // fulde exempel er under denne her method


        String apiData = webClient
                .get()
                .uri(apiBaseUrl)   // <-- use the injected property, not the template string
                .retrieve()
                .bodyToMono(String.class)
                .block();

        CISData entity = new CISData();
        entity.setFetchedAt(LocalDateTime.now());
        entity.setBody(apiData);

        CISData data = repo.save(entity);
        System.out.println("In create data" + data.getBody());
        return data;
    }
//    public CISData createCISData() {
//        String apiData = webClient
//                .get()
//                .uri(apiBaseUrl)
//                .retrieve()
//                .bodyToMono(String.class)
//                .block();
//
//        try {
//            JsonNode json = objectMapper.readTree(apiData);
//            JsonNode currentWeather = json.get("current_weather");
//
//            double temperature = currentWeather.get("temperature").asDouble();
//            double windspeed = currentWeather.get("windspeed").asDouble();
//
//            CISData entity = new CISData();
//            entity.setFetchedAt(LocalDateTime.now());
//            entity.setTemperature(temperature);
//            entity.setWeather("Wind speed: " + windspeed);
//
//            return repo.save(entity);
//
//        } catch (Exception e) {
//            throw new RuntimeException("Failed to parse weather JSON", e);
//        }
//    }
    public List<CISData> getAllCisDataBodies() {
        List<CISData> data = repo.findAll();
        return data;
    }

    public String getNewestData() {
        List<CISData> data = repo.findAll();

        if (data.isEmpty()) {
            return null;
        }

        String fetchedData = data.getFirst().getBody();
        fetchedData = "\n \n Newest weather in copenhagen is: " + fetchedData;
        return fetchedData;
    }
}

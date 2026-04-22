package org.example.bff.AI.controller;


import org.example.bff.AI.service.AIService;
import org.example.bff.AI.service.ResponseDto;
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
    public ResponseEntity<ResponseDto> ask(@RequestBody QueryRequest queryRequest) {

        ResponseDto response = service.getResponse(queryRequest.prompt());

        return ResponseEntity.ok(response);
    }

    ///  TODO implementer CISData ind i prompt:
    //    JsonNode json = objectMapper.readTree(entity.getBody());
    //
    //    JsonNode cw = json.get("current_weather");
    //
    //    String promptData = String.format(
    //            "Temperature: %.1f°C, Wind: %.1f km/h, Time: %s",
    //            cw.get("temperature").asDouble(),
    //            cw.get("windspeed").asDouble(),
    //            cw.get("time").asText()
    //    );

    //    så
    //    String prompt = """
    //Here is current weather data:
    //
    //Temperature: %.1f°C
    //Wind speed: %.1f km/h
    //Time: %s
    //
    //Describe the weather in a natural sentence.
    //""".formatted(temp, wind, time);
}

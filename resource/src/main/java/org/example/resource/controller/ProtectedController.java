package org.example.resource.controller;


import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/protected")
public class ProtectedController {

    private static final Logger LOG = LoggerFactory.getLogger(ProtectedController.class);

    public record ProtectedResponse(String message) {

    }

    @GetMapping
    public ProtectedResponse getProtected() {
        return new ProtectedResponse(String.format("%s - protected resource"));
    }
}

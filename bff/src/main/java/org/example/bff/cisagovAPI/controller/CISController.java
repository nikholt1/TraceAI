package org.example.bff.cisagovAPI.controller;


import org.example.bff.cisagovAPI.model.CISData;
import org.example.bff.cisagovAPI.service.CISDataService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/api/cisdata")
public class CISController {
    private final CISDataService service;

    public CISController(CISDataService service) {
        this.service = service;
    }

    @PostMapping
    public void fetchAndStoreData() {
        service.createCISData();
    }

    public record CisMessage(String message){}
    @GetMapping("/get")
    public ResponseEntity<List<CISData>> getCisMessages() {
//        CISData data = new CISData();
//        data.setFetchedAt(LocalDateTime.now());
//        data.setId(1L);
//        data.setBody("this is a test");
        List<CISData> datas = service.getAllCisDataBodies();


//        return ResponseEntity.of(Optional.ofNullable(data));
        return ResponseEntity.of(Optional.ofNullable(service.getAllCisDataBodies()));
    }
}

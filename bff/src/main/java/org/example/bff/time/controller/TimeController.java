package org.example.bff.time.controller;

import org.example.bff.time.model.TimeEntity;
import org.example.bff.time.service.TimeService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDateTime;

@RestController
@RequestMapping("/api/time")
public class TimeController {

    private final TimeService timeService;

    public TimeController(TimeService timeService) {
        this.timeService = timeService;
    }

    public record TimeDTO(Long id, LocalDateTime logTime) {}

    @GetMapping("/getTime/{id}")
    public ResponseEntity<TimeDTO> getTime(@PathVariable Long id) {

        return timeService.getTime(id)
                .map(this::toDTO)
                .map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/create")
    public ResponseEntity<TimeDTO> createTime(@RequestBody TimeDTO dto) {

        TimeEntity entity = toEntity(dto);
        TimeEntity saved = timeService.createTime(entity);

        return ResponseEntity.ok(toDTO(saved));
    }

    @PostMapping("/createNow")
    public ResponseEntity<TimeDTO> createNowTime() {
        LocalDateTime now = LocalDateTime.now();
        TimeEntity timeEntity = new  TimeEntity();
        timeEntity.setTime(now);

        TimeEntity saved = timeService.createTime(timeEntity);
        return ResponseEntity.ok(toDTO(saved));
    }

    private TimeDTO toDTO(TimeEntity entity) {
        return new TimeDTO(
                entity.getId(),
                entity.getTime()
        );
    }
    private TimeEntity toEntity(TimeDTO dto) {
        TimeEntity entity = new TimeEntity();
        entity.setTime(dto.logTime());
        return entity;
    }
}

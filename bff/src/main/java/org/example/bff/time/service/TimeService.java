package org.example.bff.time.service;

import org.example.bff.time.model.TimeEntity;
import org.example.bff.time.repository.TimeRepository;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;

@Service
public class TimeService {

    private final TimeRepository timeRepository;

    public TimeService(TimeRepository timeRepository) {
        this.timeRepository = timeRepository;
    }

    public Optional<TimeEntity> getTime(Long id) {
        return timeRepository.findById(id);
    }

    public TimeEntity createTime(TimeEntity timeEntity) {
        return timeRepository.save(timeEntity);
    }
}
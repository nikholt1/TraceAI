package org.example.bff.time.model;



import jakarta.persistence.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Entity
public class TimeEntity {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime logTime;


    public TimeEntity() {
    }

    public void setTime(LocalDateTime logTime) {
        this.logTime = logTime;
    }

    public LocalDateTime getTime() {
        return logTime;
    }


    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }



}

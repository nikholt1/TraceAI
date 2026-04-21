package org.example.bff.cisagovAPI.model;


import jakarta.persistence.*;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;

@Entity
@Table(name = "cisa_kev_feed")
public class CISData {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private LocalDateTime fetchedAt;

    @Lob
    @Column(name = "body", nullable = false)
    private String body; // raw JSON text

    public CISData() {
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public LocalDateTime getFetchedAt() {
        return fetchedAt;
    }

    public void setFetchedAt(LocalDateTime fetchedAt) {
        this.fetchedAt = fetchedAt;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }
}

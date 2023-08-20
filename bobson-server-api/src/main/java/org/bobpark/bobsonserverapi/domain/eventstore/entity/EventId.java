package org.bobpark.bobsonserverapi.domain.eventstore.entity;

import java.io.Serializable;
import java.time.LocalDate;
import java.util.UUID;

import jakarta.persistence.Embeddable;

import lombok.EqualsAndHashCode;
import lombok.Getter;

@EqualsAndHashCode
@Getter
@Embeddable
public class EventId implements Serializable {

    private String id;

    public EventId() {
        this.id = generateId();
    }

    public EventId(String id) {
        this.id = id;
    }

    private String generateId() {
        int year = LocalDate.now().getYear();

        return String.format("%04d-%s", year, UUID.randomUUID());
    }

    @Override
    public String toString() {
        return this.id;
    }
}

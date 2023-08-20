package org.bobpark.bobsonserverapi.domain.eventstore.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import org.bobpark.bobsonserverapi.domain.eventstore.entity.Event;
import org.bobpark.bobsonserverapi.domain.eventstore.entity.EventId;
import org.bobpark.bobsonserverapi.domain.eventstore.repository.query.EventQueryRepository;

public interface EventRepository extends JpaRepository<Event, EventId>, EventQueryRepository {
}

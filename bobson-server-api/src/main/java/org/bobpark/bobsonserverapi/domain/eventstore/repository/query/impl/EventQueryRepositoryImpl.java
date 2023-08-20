package org.bobpark.bobsonserverapi.domain.eventstore.repository.query.impl;

import static org.bobpark.bobsonserverapi.domain.eventstore.entity.QEvent.*;

import java.util.Optional;

import jakarta.persistence.LockModeType;

import lombok.RequiredArgsConstructor;

import com.querydsl.jpa.impl.JPAQueryFactory;

import org.bobpark.bobsonserverapi.common.type.EventStatus;
import org.bobpark.bobsonserverapi.domain.eventstore.entity.Event;
import org.bobpark.bobsonserverapi.domain.eventstore.repository.query.EventQueryRepository;

@RequiredArgsConstructor
public class EventQueryRepositoryImpl implements EventQueryRepository {

    private final JPAQueryFactory query;

    @Override
    public Optional<Event> fetchEvent(String eventName, String ipAddress) {
        return Optional.ofNullable(
            query.selectFrom(event)
                .where(
                    event.eventName.eq(eventName),
                    event.executedIpAddress.coalesce("NULL").in(ipAddress, "NULL"),
                    event.status.in(EventStatus.WAITING, EventStatus.PROCEEDING))
                .limit(1)
                .offset(0)
                .orderBy(event.createdDate.asc())
                .setLockMode(LockModeType.OPTIMISTIC_FORCE_INCREMENT)
                .fetchOne());
    }
}

package org.bobpark.bobsonserverapi.domain.eventstore.cqrs.listener;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import org.bobpark.bobsonserverapi.domain.eventstore.cqrs.event.CreatedEvent;
import org.bobpark.bobsonserverapi.domain.eventstore.entity.Event;
import org.bobpark.bobsonserverapi.domain.eventstore.repository.EventRepository;

@Slf4j
@RequiredArgsConstructor
@Component
@Transactional(readOnly = true)
public class CreatedEventListener {

    private final EventRepository eventRepository;

    @Async
    @Transactional
    @EventListener
    public void createEvent(CreatedEvent createdEvent) {

        Event created =
            Event.builder()
                .id(createdEvent.id())
                .eventName(createdEvent.eventName())
                .eventData(createdEvent.eventData())
                .createdModuleName(createdEvent.createdModuleName())
                .createdIpAddress(createdEvent.createdIpAddress())
                .build();

        eventRepository.save(created);

        log.debug("created event. (id={})", created.getId());

    }

}

package org.bobpark.bobsonclient.sample.domain.cqrs.aggregate;

import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Component;

import org.bobpark.bobsonclient.event.annotation.EventSourcingHandler;
import org.bobpark.bobsonclient.sample.domain.cqrs.event.CreatedSampleEvent;

@Slf4j
@Component
public class SampleAggregate {

    @EventSourcingHandler(CreatedSampleEvent.class)
    public void handleCreatedSample(CreatedSampleEvent createdEvent) {

        log.debug("created event. ({})", createdEvent);
    }

}

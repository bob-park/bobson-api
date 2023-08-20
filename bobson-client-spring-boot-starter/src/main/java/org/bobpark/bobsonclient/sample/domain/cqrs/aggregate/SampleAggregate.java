package org.bobpark.bobsonclient.sample.domain.cqrs.aggregate;

import lombok.extern.slf4j.Slf4j;

import org.bobpark.bobsonclient.event.annotation.Aggregate;
import org.bobpark.bobsonclient.event.annotation.CommandHandler;
import org.bobpark.bobsonclient.event.annotation.EventSourcingHandler;
import org.bobpark.bobsonclient.sample.domain.cqrs.event.CreatedSampleEvent;

@Slf4j
@Aggregate
public class SampleAggregate {

    @CommandHandler("CreatedSampleEvent")
    public void handleCreateSample(CreateSampleCommand command){

    }

    @EventSourcingHandler
    public void handleCreatedSample(CreatedSampleEvent createdEvent) {

        log.debug("created event. ({})", createdEvent);
    }

}

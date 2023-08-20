package org.bobpark.bobsonserverapi.exception;

import org.bobpark.bobsonserverapi.domain.eventstore.entity.EventId;

public class AlreadyExecutedEventException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Already executed event.";

    public AlreadyExecutedEventException() {
        super(DEFAULT_MESSAGE);
    }

    public AlreadyExecutedEventException(EventId id) {
        super(DEFAULT_MESSAGE + "(id=" + id + ")");
    }
}

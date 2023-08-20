package org.bobpark.bobsonserverapi.exception;

import org.bobpark.bobsonserverapi.common.type.EventStatus;
import org.bobpark.bobsonserverapi.domain.eventstore.entity.EventId;

public class InvalidEventStatusException extends RuntimeException {

    private static final String DEFAULT_MESSAGE = "Invalid event status.";

    public InvalidEventStatusException() {
        super(DEFAULT_MESSAGE);
    }

    public InvalidEventStatusException(EventId id, EventStatus status) {
        super(DEFAULT_MESSAGE + "(id=" + id + ", status=" + status + ")");
    }
}

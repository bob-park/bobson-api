package org.bobpark.bobsonserverapi.domain.eventstore.model;

public record CompleteEventRequest(boolean isSuccess,
                                   String message) {
}

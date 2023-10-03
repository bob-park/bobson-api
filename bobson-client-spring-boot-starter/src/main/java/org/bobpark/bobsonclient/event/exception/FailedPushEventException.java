package org.bobpark.bobsonclient.event.exception;

public class FailedPushEventException extends RuntimeException{

    private static final String DEFAULT_MESSAGE = "Failed push event.";

    public FailedPushEventException() {
        super(DEFAULT_MESSAGE);
    }

    public FailedPushEventException(String defaultMessage) {
        super(DEFAULT_MESSAGE + " - " + defaultMessage);
    }
}

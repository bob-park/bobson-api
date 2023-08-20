package org.bobpark.bobsonclient.event.client.model;

public interface EventCommand {

    default String serialId() {
        return null;
    }
}

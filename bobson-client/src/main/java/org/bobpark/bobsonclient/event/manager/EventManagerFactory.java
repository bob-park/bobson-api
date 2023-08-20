package org.bobpark.bobsonclient.event.manager;

import org.springframework.context.ApplicationEventPublisher;

import com.fasterxml.jackson.databind.ObjectMapper;

import org.bobpark.bobsonclient.configure.properties.BobsonClientProperties;
import org.bobpark.bobsonclient.event.client.BobSonApiClient;

public interface EventManagerFactory {

    static EventManagerBuilder builder() {
        return new EventManagerBuilder();
    }

    class EventManagerBuilder {
        private ObjectMapper om;
        private BobsonClientProperties properties;
        private BobSonApiClient apiClient;
        private ApplicationEventPublisher eventPublisher;

        public EventManagerBuilder objectMapper(ObjectMapper om) {
            this.om = om;

            return this;
        }

        public EventManagerBuilder properties(BobsonClientProperties properties) {
            this.properties = properties;

            return this;
        }

        public EventManagerBuilder apiClient(BobSonApiClient apiClient) {
            this.apiClient = apiClient;

            return this;
        }

        public EventManagerBuilder eventPublisher(ApplicationEventPublisher eventPublisher) {
            this.eventPublisher = eventPublisher;

            return this;
        }

        public EventManager build() {
            return new EventManager(om, properties, apiClient, eventPublisher);
        }
    }
}

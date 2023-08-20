package org.bobpark.bobsonclient.event.client.impl;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.http.RequestEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import org.bobpark.bobsonclient.configure.properties.BobsonClientProperties;
import org.bobpark.bobsonclient.event.client.BobSonApiClient;
import org.bobpark.bobsonclient.event.client.model.CreateEventRequest;
import org.bobpark.bobsonclient.event.client.model.EventResponse;
import org.bobpark.bobsonclient.event.client.model.FetchEventRequest;
import org.bobpark.bobsonclient.event.client.model.impl.DefaultCreateEventRequest;
import org.bobpark.bobsonclient.event.client.model.impl.DefaultEventResponse;
import org.bobpark.bobsonclient.event.client.model.impl.DefaultFetchEventRequest;
import org.bobpark.bobsonclient.event.exception.FailedPushEventException;

@Slf4j
@RequiredArgsConstructor
public class DefaultEventStoreApiClient implements BobSonApiClient {

    private static final String PUSH_API = "/event";
    private static final String FETCH_API = "/event/fetch";
    private static final String COMPLETE_API = "/event/complete/{eventId}";

    private final BobsonClientProperties properties;
    private final RestTemplate restTemplate;

    @Override
    public EventResponse push(CreateEventRequest pushData) {

        DefaultCreateEventRequest createRequest = (DefaultCreateEventRequest)pushData;

        RequestEntity<DefaultCreateEventRequest> requestEntity =
            RequestEntity.post(includeUri(PUSH_API))
                .body(createRequest);

        return request(requestEntity);
    }

    @Override
    public EventResponse fetch(String eventName) {

        URI uri =
            UriComponentsBuilder.fromUriString(includeUri(FETCH_API))
                .queryParam("eventName", eventName)
                .queryParam("moduleName", properties.getInstanceId())
                .build()
                .toUri();

        RequestEntity<Void> requestEntity =
            RequestEntity.get(uri)
                .build();

        return request(requestEntity);

    }

    @Override
    public EventResponse complete(String eventId, boolean isSuccess, String detailMessage) {

        Map<String, Object> body = new HashMap<>();

        body.put("isSuccess", isSuccess);
        body.put("message", detailMessage);

        RequestEntity<Map<String, Object>> requestEntity =
            RequestEntity.put(includeUri(COMPLETE_API), eventId)
                .body(body);

        return request(requestEntity);
    }

    private DefaultEventResponse request(RequestEntity<?> requestEntity) {
        ResponseEntity<DefaultEventResponse> response =
            restTemplate.exchange(requestEntity, DefaultEventResponse.class);

        if (!response.getStatusCode().is2xxSuccessful()) {
            throw new FailedPushEventException();
        }

        return response.getBody();
    }

    private String includeUri(String uri) {
        return properties.getHost() + uri;
    }

}

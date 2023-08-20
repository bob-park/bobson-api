package org.bobpark.bobsonclient.event.manager;

import static org.apache.commons.lang3.ObjectUtils.*;

import java.util.Collections;
import java.util.Map;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.context.ApplicationEventPublisher;

import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import io.micrometer.common.util.StringUtils;

import org.bobpark.bobsonclient.configure.properties.BobsonClientProperties;
import org.bobpark.bobsonclient.event.annotation.CommandHandler;
import org.bobpark.bobsonclient.event.annotation.EventSourcingHandler;
import org.bobpark.bobsonclient.event.client.BobSonApiClient;
import org.bobpark.bobsonclient.event.client.model.CreateEventRequest;
import org.bobpark.bobsonclient.event.client.model.EventResponse;
import org.bobpark.bobsonclient.event.client.model.impl.DefaultCreateEventRequest;
import org.bobpark.core.exception.ServiceRuntimeException;

@Slf4j
@RequiredArgsConstructor
@Aspect
public class EventManager {

    private final ObjectMapper om;
    private final BobsonClientProperties properties;
    private final BobSonApiClient apiClient;
    private final ApplicationEventPublisher eventPublisher;

    @PostConstruct
    public void init() {
        log.info("Initialize BobSon Event Store Manager. (host={}, instanceId={})", properties.getHost(),
            properties.getInstanceId());
    }

    @After("@annotation(commandHandler)")
    public void push(JoinPoint joinPoint, CommandHandler commandHandler) {

        Object[] args = joinPoint.getArgs();

        if (args.length == 0) {
            log.warn("No exist event command.");
            return;
        }

        Object eventCommand = args[0];

        CreateEventRequest createEventRequest =
            DefaultCreateEventRequest.builder()
                .eventName(eventCommand.getClass().getSimpleName())
                .createdModuleName(properties.getInstanceId())
                .eventData(parseToMap(eventCommand))
                .build();

        apiClient.push(createEventRequest);

        log.debug("pushed event. (name={})", createEventRequest.getEventName());

    }

    @Around("@annotation(eventSourcingHandler)")
    public Object fetchEvent(ProceedingJoinPoint joinPoint, EventSourcingHandler eventSourcingHandler) throws
        Throwable {

        Object[] args = joinPoint.getArgs();

        if (args.length == 0) {
            log.warn("No event mapping class.");
            return null;
        }

        String eventName = eventSourcingHandler.value().getSimpleName();

        Object event = args[0];

        EventResponse eventResponse = apiClient.fetch(eventName);

        if (isEmpty(eventResponse) || StringUtils.isBlank(eventResponse.getId())) {
            return null;
        }

        event = castEvent(eventResponse.getEventData(), event.getClass());

        eventPublisher.publishEvent(event);

        log.debug("fetch event. (id={}, eventName={})", eventResponse.getId(), eventResponse.getEventName());

        return joinPoint.proceed();
    }

    private Map<String, Object> parseToMap(Object obj) {

        if (obj == null) {
            return Collections.emptyMap();
        }

        try {
            String str = om.writeValueAsString(obj);

            return om.readValue(str, new TypeReference<>() {
            });

        } catch (JsonProcessingException e) {
            log.error("Parse Error - {}", e.getMessage(), e);
            throw new ServiceRuntimeException(e);
        }
    }

    private Object castEvent(Map<String, Object> data, Class<?> eventClass) {

        if (data == null || data.isEmpty()) {
            return null;
        }

        try {
            String str = om.writeValueAsString(data);

            return om.readValue(str, eventClass);
        } catch (JsonProcessingException e) {
            log.error("Parse Error - {}", e.getMessage(), e);
            throw new ServiceRuntimeException(e);
        }
    }

}

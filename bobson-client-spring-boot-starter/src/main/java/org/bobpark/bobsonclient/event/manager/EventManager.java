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
import org.bobpark.bobsonclient.event.client.model.FetchEventRequest;
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
                .eventName(commandHandler.pushEvent().getSimpleName())
                .createdModuleName(properties.getInstanceId())
                .eventData(parseToMap(eventCommand))
                .build();

        apiClient.push(createEventRequest);

        log.debug("pushed event. (name={})", createEventRequest.getEventName());

    }

    @Around("@annotation(eventSourcingHandler)")
    public Object fetchEvent(ProceedingJoinPoint joinPoint, EventSourcingHandler eventSourcingHandler) throws
        Throwable {

        Object retVal = null;
        Object[] args = joinPoint.getArgs();

        if (args.length == 0) {
            log.warn("No event mapping class.");
            return retVal;
        }

        Object event = args[0];

        String eventName = null;

        Class<?>[] events = eventSourcingHandler.classes();

        if (events.length > 0) {
            eventName = events[0].getSimpleName();
        } else {
            eventName = event.getClass().getSimpleName();
        }

        EventResponse eventResponse = apiClient.fetch(eventName);

        if (isEmpty(eventResponse) || StringUtils.isBlank(eventResponse.getId())) {
            log.warn("No event.");
            return retVal;
        }

        event = castEvent(eventResponse.getEventData(), event.getClass());
        args[0] = event;

        eventPublisher.publishEvent(event);

        log.debug("fetch event. (id={}, eventName={})", eventResponse.getId(), eventResponse.getEventName());

        boolean isSuccess = false;
        String message = null;

        try {
            retVal = joinPoint.proceed(args);
            isSuccess = true;
        } catch (Exception e) {
            message = e.getMessage();
            throw new ServiceRuntimeException(e);
        } finally {
            apiClient.complete(eventResponse.getId(), isSuccess, message);
        }

        return retVal;
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

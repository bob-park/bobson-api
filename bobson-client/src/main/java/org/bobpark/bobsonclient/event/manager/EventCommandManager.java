package org.bobpark.bobsonclient.event.manager;

import java.util.Arrays;
import java.util.Collections;
import java.util.Map;

import jakarta.annotation.PostConstruct;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.apache.commons.lang3.ClassUtils;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.annotation.After;
import org.aspectj.lang.annotation.Aspect;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

import org.bobpark.bobsonclient.configure.properties.BobsonClientProperties;
import org.bobpark.bobsonclient.event.annotation.CommandHandler;
import org.bobpark.bobsonclient.event.client.BobSonApiClient;
import org.bobpark.bobsonclient.event.client.model.CreateEventRequest;
import org.bobpark.bobsonclient.event.client.model.EventCommand;
import org.bobpark.bobsonclient.event.client.model.impl.DefaultCreateEventRequest;
import org.bobpark.core.exception.ServiceRuntimeException;

@Slf4j
@RequiredArgsConstructor
@Aspect
public class EventCommandManager {

    private final ObjectMapper om;
    private final BobsonClientProperties properties;
    private final BobSonApiClient apiClient;

    @PostConstruct
    public void init() {
        log.info("Initialize BobSon Event Store Manager. (host={}, instanceId={})", properties.getHost(), properties.getInstanceId());
    }

    @After("@annotation(commandHandler)")
    public void push(JoinPoint joinPoint, CommandHandler commandHandler) {

        Object[] args = joinPoint.getArgs();

        EventCommand eventCommand = Arrays.stream(args)
            .filter(arg -> ClassUtils.isAssignable(arg.getClass(), EventCommand.class))
            .map(arg -> (EventCommand)arg)
            .findAny()
            .orElse(null);

        if (eventCommand == null) {
            log.warn("No exist event command.");
            return;
        }

        CreateEventRequest createEventRequest =
            DefaultCreateEventRequest.builder()
                .eventName(eventCommand.getClass().getSimpleName())
                .createdModuleName(properties.getInstanceId())
                .eventData(parseToMap(eventCommand))
                .build();

        apiClient.push(createEventRequest);

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

}

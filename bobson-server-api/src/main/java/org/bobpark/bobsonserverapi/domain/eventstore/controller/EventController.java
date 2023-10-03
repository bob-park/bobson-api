package org.bobpark.bobsonserverapi.domain.eventstore.controller;

import lombok.RequiredArgsConstructor;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import org.bobpark.bobsonserverapi.domain.eventstore.entity.EventId;
import org.bobpark.bobsonserverapi.domain.eventstore.model.CompleteEventRequest;
import org.bobpark.bobsonserverapi.domain.eventstore.model.CreateEventRequest;
import org.bobpark.bobsonserverapi.domain.eventstore.model.EventResponse;
import org.bobpark.bobsonserverapi.domain.eventstore.service.EventService;

@RequiredArgsConstructor
@RestController
@RequestMapping("event")
public class EventController {

    private final EventService eventService;

    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping(path = "")
    public EventResponse createEvent(@RequestBody CreateEventRequest createRequest) {
        return eventService.createEvent(createRequest);
    }

    @GetMapping(path = "fetch")
    public EventResponse fetch(@RequestParam("eventName") String eventName,
        @RequestParam("moduleName") String moduleName) {
        return eventService.fetch(eventName, moduleName);
    }

    @PutMapping(path = "complete/{eventId}")
    public EventResponse complete(@PathVariable String eventId, @RequestBody CompleteEventRequest completeRequest) {
        return eventService.complete(new EventId(eventId), completeRequest);
    }
}

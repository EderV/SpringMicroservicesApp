package com.evm.ms.userevents.infrastructure.controllers;

import com.evm.ms.userevents.domain.Event;
import com.evm.ms.userevents.domain.ports.in.UserEventServicePort;
import com.evm.ms.userevents.infrastructure.dto.request.EventRequest;
import com.evm.ms.userevents.infrastructure.mappers.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/ms/user/event")
@RequiredArgsConstructor
public class UserEventsController {

    private final UserEventServicePort userEventServicePort;

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Test OK");
    }

    @GetMapping("/all")
    public ResponseEntity<List<EventRequest>> getAll() {
        var res = userEventServicePort.getAllEvents();
        var listRequest = res.stream().map(this::toEventRequest).toList();

        return ResponseEntity.ok(listRequest);
    }

    @PostMapping
    public ResponseEntity<?> addNewEvent(@RequestBody EventRequest eventRequest) {
        log.error("ZonedDateTime: " + eventRequest.getEventDateTime().toString() + ", TimeZone: " + eventRequest.getEventDateTime().getZone());

        var event = toEvent(eventRequest);
        userEventServicePort.addNewEvent(event);

        return ResponseEntity.ok("Event " + eventRequest.getTitle() + " saved in DB");
    }

    @PutMapping
    public ResponseEntity<?> updateExistingEvent(@RequestBody EventRequest eventRequest) {
        var event = toEvent(eventRequest);

        var updated = userEventServicePort.updateEvent(event);
        if (updated) {
            return ResponseEntity.ok("Event updated");
        }

        return new ResponseEntity<>("Error updating event", HttpStatus.BAD_REQUEST);
    }

    private Event toEvent(EventRequest eventRequest) {
        return EventMapper.MAPPER.toEvent(eventRequest);
    }

    private EventRequest toEventRequest(Event event) {
        return EventMapper.MAPPER.toEventRequest(event);
    }

}

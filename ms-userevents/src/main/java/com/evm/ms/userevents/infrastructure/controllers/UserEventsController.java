package com.evm.ms.userevents.infrastructure.controllers;

import com.evm.ms.userevents.domain.Event;
import com.evm.ms.userevents.domain.ports.in.UserEventServicePort;
import com.evm.ms.userevents.infrastructure.dto.request.EventRequest;
import com.evm.ms.userevents.infrastructure.mappers.EventMapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

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

    @PostMapping
    public ResponseEntity<?> addNewEvent(@RequestBody EventRequest eventRequest) {
        log.error("ZonedDateTime: " + eventRequest.getEventDateTime().toString() + ", TimeZone: " + eventRequest.getEventDateTime().getZone());

        var event = toEvent(eventRequest);
        userEventServicePort.addNewEvent(event);

        return ResponseEntity.ok("Event " + eventRequest.getTitle() + " saved in DB");
    }

    private Event toEvent(EventRequest eventRequest) {
        return EventMapper.MAPPER.toEvent(eventRequest);
    }

}

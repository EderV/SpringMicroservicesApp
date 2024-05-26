package com.evm.ms.scheduler.infrastructure.controllers;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("ms/scheduler")
public class SchedulerController {

    @GetMapping
    public ResponseEntity<String> test() {
        return ResponseEntity.ok("Scheduler controller OK");
    }

}

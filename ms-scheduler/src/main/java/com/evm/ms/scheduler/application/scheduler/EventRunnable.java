package com.evm.ms.scheduler.application.scheduler;

import com.evm.ms.scheduler.domain.Event;
import com.evm.ms.scheduler.domain.EventScheduleData;
import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;
import java.util.function.Consumer;
import java.util.stream.Collectors;

@Slf4j
public class EventRunnable implements Runnable {

    private final EventScheduleData eventScheduleData;
    private final Consumer<Event> alertCallback;

    public EventRunnable(EventScheduleData eventScheduleData, Consumer<Event> alertCallback) {
        this.eventScheduleData = eventScheduleData;
        this.alertCallback = alertCallback;

        this.eventScheduleData.getDelays().forEach((s) -> log.error("Seconds: {}", s));
    }

    @Override
    public void run() {

        var title = eventScheduleData.getEvent().getTitle();
        var delays = eventScheduleData.getDelays();

        var stringifyDelays = delays.stream().map(String::valueOf).collect(Collectors.joining("s, "));
        log.info("Event: {} - Delays: {}", title, stringifyDelays);

        try {
            for (Long delay : eventScheduleData.getDelays()) {
                log.info("Waiting for {} seconds", delay);
                TimeUnit.SECONDS.sleep(delay);

                log.warn("Time triggered");
                alertCallback.accept(eventScheduleData.getEvent());
            }

            log.info("Delays for '{}' finished", title);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("EventRunnable {} interrupted", eventScheduleData.getEvent().getTitle());
        }
    }


}

package com.evm.ms.scheduler.application.scheduler;

import com.evm.ms.scheduler.application.utils.EventScheduleUtils;
import com.evm.ms.scheduler.domain.Event;
import com.evm.ms.scheduler.domain.EventScheduleData;
import com.evm.ms.scheduler.domain.ports.MessengerOutputPort;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.*;
import java.util.function.Consumer;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventSchedulerDefault implements EventScheduler {

    private final EventScheduleUtils eventScheduleUtils;
    private final MessengerOutputPort messengerOutputPort;

    private ExecutorService executor;
    private Consumer<Event> alertCallback;

    private final Map<String, Future<?>> scheduledEvents = new HashMap<>();

    @Override
    @PostConstruct
    public void start() {
        executor = new ThreadPoolExecutor(
                0, Integer.MAX_VALUE,
                10L, TimeUnit.SECONDS,
                new SynchronousQueue<>());

        // Initialized here to avoid DI problems with MessengerOutputPort
        alertCallback = (Event event) -> {
            var currentTime = System.currentTimeMillis() / 1000;
            var eventTime = event.getEventDateTime().toEpochSecond();

            if (currentTime >= eventTime) {
                scheduledEvents.remove(event.getId());
                messengerOutputPort.sendEventFinished(event);
                return;
            }

            messengerOutputPort.sendEventNotice(event);
        };
    }

    @Override
    public void scheduleEvent(Event event) {
        if (event.getTriggered()) {
            return;
        }

        var offset = System.currentTimeMillis() / 1000;
        var preNotices = event.getPreNotices();
        var triggerDate = event.getEventDateTime();
        var delays = eventScheduleUtils.calculateDelayFromOffset(offset, preNotices, triggerDate);

        var eventScheduleData = new EventScheduleData(event, delays);

        var runnable = new EventRunnable(eventScheduleData, alertCallback);
        var future = executor.submit(runnable);
        scheduledEvents.put(event.getId(), future);
    }

    @Override
    public void modifyScheduledEvent(Event event) {
        if (event.getTriggered()) {
            return;
        }

        var id = event.getId();
        if (scheduledEvents.containsKey(id)) {
            var future = scheduledEvents.get(id);
            future.cancel(true);
            scheduledEvents.remove(id);
        }

        this.scheduleEvent(event);
    }

    @Override
    public void finish() {
        executor.shutdownNow();
    }

    @PreDestroy
    public void preDestroy() {
        log.info("Shutting down EventScheduler executor service");
        this.finish();
    }

}

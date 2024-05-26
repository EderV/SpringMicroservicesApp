package com.evm.ms.scheduler.application.scheduler;

import com.evm.ms.scheduler.application.utils.TimeUtils;
import com.evm.ms.scheduler.domain.Event;
import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.SynchronousQueue;
import java.util.concurrent.ThreadPoolExecutor;
import java.util.concurrent.TimeUnit;

@Slf4j
@Service
@RequiredArgsConstructor
public class EventSchedulerDefault implements EventScheduler {

    private final TimeUtils timeUtils;

    private ExecutorService executor;

    @Override
    @PostConstruct
    public void start() {
        executor = new ThreadPoolExecutor(
                0, Integer.MAX_VALUE,
                10L, TimeUnit.SECONDS,
                new SynchronousQueue<>());
    }

    @Override
    public void scheduleEvent(Event event) {
        if (event.getTriggered()) {
            return;
        }

        var eventTitle = event.getTitle();
        var offset = System.currentTimeMillis() / 1000;
        var preNotices = event.getPreNotices();
        var triggerDate = event.getEventDateTime();
        var delays = timeUtils.calculateDelayFromOffset(offset, preNotices, triggerDate);

        var runnable = new EventRunnable(eventTitle, delays);
        executor.execute(runnable);
    }

    @Override
    public void modifyScheduledEvent(Event event) {

    }

    @Override
    public void finish() {
        executor.shutdownNow();
    }

    @PreDestroy
    public void preDestroy() {
        log.info("Shutting down EventScheduler executor service");
        executor.shutdownNow();
    }

}

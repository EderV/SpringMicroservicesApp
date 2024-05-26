package com.evm.ms.scheduler.application.scheduler;

import lombok.extern.slf4j.Slf4j;

import java.util.List;
import java.util.concurrent.TimeUnit;

@Slf4j
public class EventRunnable implements Runnable {

    private final List<Long> delays;
    private final String title;

    public EventRunnable(String title, List<Long> delays) {
        this.delays = delays;
        this.title = title;

        this.delays.forEach((s) -> log.error("Seconds: {}", s));
    }

    @Override
    public void run() {
        try {
            for (Long delay : delays) {
                log.info("Waiting for {} seconds", delay);
                TimeUnit.SECONDS.sleep(delay);

                log.warn("Time triggered");
            }

            log.warn("Delays finished");
//            long currentTime = System.currentTimeMillis() / 1000;
//            long initialSleepTime = triggerTime - currentTime - secondsList.get(0);
//
//            log.error("Current time: {}", currentTime);
//            log.error("Initial sleep time: {}", initialSleepTime);
//
//            if (initialSleepTime > 0) {
//                TimeUnit.SECONDS.sleep(initialSleepTime);
//                System.out.println("Evento en " + secondsList.get(0) + " segundos.");
//            }
//
//            for (int i = 1; i < secondsList.size(); i++) {
//                long nextSleepTime = (secondsList.get(i) - secondsList.get(i - 1));
//                if (nextSleepTime > 0) {
//                    TimeUnit.SECONDS.sleep(nextSleepTime);
//                    System.out.println("Evento en " + secondsList.get(i) + " segundos.");
//                }
//            }
//
//            long finalSleepTime = triggerTime - System.currentTimeMillis() / 1000;
//            if (finalSleepTime > 0) {
//                TimeUnit.SECONDS.sleep(finalSleepTime);
//            }
//            System.out.println("Evento final.");
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            log.error("EventRunnable {} interrupted", title);
        }
    }


}

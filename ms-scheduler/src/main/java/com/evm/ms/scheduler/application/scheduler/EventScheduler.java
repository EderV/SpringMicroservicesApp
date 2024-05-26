package com.evm.ms.scheduler.application.scheduler;

import com.evm.ms.scheduler.domain.Event;

public interface EventScheduler extends Scheduler {

    void scheduleEvent(Event event);

    void modifyScheduledEvent(Event event);

}

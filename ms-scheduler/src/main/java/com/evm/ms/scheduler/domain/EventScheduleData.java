package com.evm.ms.scheduler.domain;

import lombok.Data;

import java.util.List;

@Data
public class EventScheduleData {

    private final Event event;
    private final List<Long> delays;

}

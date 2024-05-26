package com.evm.ms.scheduler.application.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class TimeUtils {

    /**
     * Calculates an array of delays containing the delay from the offset to the preNotices times and trigger time
     * @param offset from when you want to calculate the delays. Offset from epoc time
     * @param preNotices events previous to trigger time in minutes
     * @param triggerTime date of the final event
     * @return a list of delays from the offset time to be waited sequentially using a for loop. If the trigger time
     * is after now, it will return an array with one item and with value 0L
     */
    public List<Long> calculateDelayFromOffset(long offset, List<Integer> preNotices, ZonedDateTime triggerTime) {
        var delays = new ArrayList<Long>();

        if (triggerTime.isBefore(ZonedDateTime.now())) {
            return List.of(0L);
        }

        var triggerTimeEpochSecond = triggerTime.toEpochSecond();

        if (preNotices != null && !preNotices.isEmpty()) {
            var preNoticesSeconds = preNotices.stream()
                    .filter(num -> num != 0)
                    .distinct()
                    .sorted((a, b) -> b - a)
                    .map(n -> n * 60)
                    .toList();

            preNoticesSeconds.forEach((n) -> log.error("PreNotices: {}", n));

            for (int i = 0; i < preNoticesSeconds.size(); i++) {
                long delay;
                if (i == 0) {
                    delay = triggerTimeEpochSecond - preNoticesSeconds.get(i) - offset;
                }
                else {
                    delay = preNoticesSeconds.get(i - 1) - preNoticesSeconds.get(i);
                }
                delays.add(delay);
            }

            var lastPreNotice = (long) preNoticesSeconds.get(preNotices.size() - 1);
            delays.add(lastPreNotice);
        }
        else {
            var delay = triggerTimeEpochSecond - offset;
            delays.add(delay);
        }

        return delays;
    }

}

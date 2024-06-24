package com.evm.ms.scheduler.application.utils;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.ZonedDateTime;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
public class EventScheduleUtils {

    /**
     * Calculates an array of delays containing the delay from the offset to the preNotices times and trigger time
     * @param offset from when you want to calculate the delays. Offset from epoc time in seconds
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
            // Delete zeros, remove duplicates, sort and convert to seconds
            var preNoticesSeconds = new ArrayList<>(preNotices.stream()
                    .filter(num -> num != 0)
                    .distinct()
                    .sorted((a, b) -> b - a)
                    .map(n -> n * 60)
                    .toList());

            int i = 0;
            while (i < preNoticesSeconds.size()) {
                long delay;
                if (i == 0) {
                    delay = triggerTimeEpochSecond - preNoticesSeconds.get(i) - offset;
                }
                else {
                    delay = preNoticesSeconds.get(i - 1) - preNoticesSeconds.get(i);
                }

                if (delay > 0) {
                    delays.add(delay);
                    i++;
                }
                else {
                    preNoticesSeconds.remove(i);
                }
            }

            // Delays array empty means that we are after last pre-notice and before trigger time
            if (delays.isEmpty()) {
                var delay = triggerTimeEpochSecond - offset;
                delays.add(delay);
            }
            else {
                var lastPreNotice = (long) preNoticesSeconds.get(preNoticesSeconds.size() - 1);
                delays.add(lastPreNotice);
            }
        }
        else {
            var delay = triggerTimeEpochSecond - offset;
            delays.add(delay);
        }

        return delays;
    }

}

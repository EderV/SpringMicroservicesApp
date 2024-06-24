package com.evm.ms.scheduler.application;

import com.evm.ms.scheduler.application.utils.EventScheduleUtils;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EventScheduleUtilsTest {

    private final EventScheduleUtils timeUtils = new EventScheduleUtils();

    private final long offset = System.currentTimeMillis() / 1000;

    @Test
    public void testCalculateDelayFromOffsetWithPreNotices() {
        List<Integer> preNotices = Arrays.asList(10, 20, 30);
        ZonedDateTime triggerTime = ZonedDateTime.now().plusHours(1);

        List<Long> expectedDelays = Arrays.asList(
                triggerTime.toEpochSecond() - (30 * 60) - offset,
                (30 - 20) * 60L,
                (20 - 10) * 60L,
                10 * 60L
        );

        List<Long> actualDelays = timeUtils.calculateDelayFromOffset(offset, preNotices, triggerTime);

        assertEquals(expectedDelays, actualDelays);
    }

    @Test
    public void testCalculateDelayFromOffsetWithoutPreNotices() {
        List<Integer> preNotices = Collections.emptyList();
        ZonedDateTime triggerTime = ZonedDateTime.now().plusHours(1);

        List<Long> expectedDelays = Collections.singletonList(triggerTime.toEpochSecond() - offset);
        List<Long> actualDelays = timeUtils.calculateDelayFromOffset(offset, preNotices, triggerTime);

        assertEquals(expectedDelays, actualDelays);
    }

    @Test
    public void testCalculateDelayFromOffsetTriggerTimeInPast() {
        List<Integer> preNotices = Arrays.asList(10, 20, 30);
        ZonedDateTime triggerTime = ZonedDateTime.now().minusHours(1);

        List<Long> expectedDelays = Collections.singletonList(0L);
        List<Long> actualDelays = timeUtils.calculateDelayFromOffset(offset, preNotices, triggerTime);

        assertEquals(expectedDelays, actualDelays);
    }

    @Test
    public void testCalculateDelayFromOffsetNullPreNotices() {
        List<Integer> preNotices = null;
        ZonedDateTime triggerTime = ZonedDateTime.now().plusHours(1);

        List<Long> expectedDelays = Collections.singletonList(triggerTime.toEpochSecond() - offset);
        List<Long> actualDelays = timeUtils.calculateDelayFromOffset(offset, preNotices, triggerTime);

        assertEquals(expectedDelays, actualDelays);
    }

    @Test
    public void testCalculateDelayFromOffsetWithPreNoticeExceedingTriggerTime() {
        List<Integer> preNotices = Arrays.asList(10, 20, 70); // 70 minutes exceeds the trigger time
        ZonedDateTime triggerTime = ZonedDateTime.now().plusMinutes(60); // Trigger time is 60 minutes from now

        // Expected delays should exclude the 70 minutes preNotice because it would result in a negative delay
        List<Long> expectedDelays = Arrays.asList(
                triggerTime.toEpochSecond() - (20 * 60) - offset,
                (20 - 10) * 60L,
                10 * 60L
        );

        List<Long> actualDelays = timeUtils.calculateDelayFromOffset(offset, preNotices, triggerTime);

        assertEquals(expectedDelays, actualDelays);
    }

    @Test
    public void testCalculateDelayFromOffsetWithAllPreNoticesExceedingTriggerTime() {
        List<Integer> preNotices = Arrays.asList(70, 80, 90); // All preNotices exceed the trigger time
        ZonedDateTime triggerTime = ZonedDateTime.now().plusMinutes(60); // Trigger time is 60 minutes from now

        // Expected delays should be just the delay to the trigger time as all preNotices are invalid
        List<Long> expectedDelays = Collections.singletonList(triggerTime.toEpochSecond() - offset);

        List<Long> actualDelays = timeUtils.calculateDelayFromOffset(offset, preNotices, triggerTime);

        assertEquals(expectedDelays, actualDelays);
    }
}

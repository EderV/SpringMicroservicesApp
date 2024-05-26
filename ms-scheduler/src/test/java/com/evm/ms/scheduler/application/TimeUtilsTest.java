package com.evm.ms.scheduler.application;

import com.evm.ms.scheduler.application.utils.TimeUtils;
import org.junit.jupiter.api.Test;

import java.time.ZonedDateTime;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class TimeUtilsTest {

    private final TimeUtils timeUtils = new TimeUtils();

    @Test
    public void testCalculateDelayFromOffsetWithPreNotices() {
        long offset = 1000L;
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
        long offset = 1000L;
        List<Integer> preNotices = Collections.emptyList();
        ZonedDateTime triggerTime = ZonedDateTime.now().plusHours(1);

        List<Long> expectedDelays = Collections.singletonList(triggerTime.toEpochSecond() - offset);
        List<Long> actualDelays = timeUtils.calculateDelayFromOffset(offset, preNotices, triggerTime);

        assertEquals(expectedDelays, actualDelays);
    }

    @Test
    public void testCalculateDelayFromOffsetTriggerTimeInPast() {
        long offset = 1000L;
        List<Integer> preNotices = Arrays.asList(10, 20, 30);
        ZonedDateTime triggerTime = ZonedDateTime.now().minusHours(1);

        List<Long> expectedDelays = Collections.singletonList(0L);
        List<Long> actualDelays = timeUtils.calculateDelayFromOffset(offset, preNotices, triggerTime);

        assertEquals(expectedDelays, actualDelays);
    }

    @Test
    public void testCalculateDelayFromOffsetNullPreNotices() {
        long offset = 1000L;
        List<Integer> preNotices = null;
        ZonedDateTime triggerTime = ZonedDateTime.now().plusHours(1);

        List<Long> expectedDelays = Collections.singletonList(triggerTime.toEpochSecond() - offset);
        List<Long> actualDelays = timeUtils.calculateDelayFromOffset(offset, preNotices, triggerTime);

        assertEquals(expectedDelays, actualDelays);
    }

}

package com.studytracker.Utility;

import java.time.Duration;

public class DurationFormatter {

    private DurationFormatter() {}

    public static String formatDuration(Duration d) {
        long hours = d.toDaysPart() * 24 + d.toHoursPart();
        int mins = d.toMinutesPart();
        int seconds = d.toSecondsPart();

        return String.format("%02d:%02d:%02d", hours, mins, seconds);
    }
}

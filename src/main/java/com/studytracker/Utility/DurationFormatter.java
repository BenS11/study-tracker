package com.studytracker.Utility;

import java.time.Duration;

public class DurationFormatter {

    private DurationFormatter() {}

    /**
     * Formats a duration to a human-readable string
     * @param d duration to format
     * @return a String in the form HH:mm:ss
     */
    public static String formatDuration(Duration d) {
        long hours = d.toDaysPart() * 24 + d.toHoursPart();
        int mins = d.toMinutesPart();
        int seconds = d.toSecondsPart();

        return String.format("%02d:%02d:%02d", hours, mins, seconds);
    }
}

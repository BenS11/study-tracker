package com.studytracker.Study;

import com.studytracker.Interfaces.CSVable;
import com.studytracker.Utility.DurationFormatter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class StudySession implements CSVable {

    public final long id;
    public final LocalDateTime date;
    public final Duration duration;
    public final String subject;

    public StudySession(long id, LocalDateTime date, Duration duration, String subject) {
        this.id = id;
        this.date = date;
        this.duration = duration;
        this.subject = subject;
    }

    public StudySession(long id, LocalDateTime startTime, LocalDateTime endTime, String subject) {
        this.id = id;
        this.date = startTime;
        this.duration = Duration.between(startTime, endTime);
        this.subject = subject;
    }

    public StudySession edit(LocalDateTime date, Duration duration, String subject) {
        if (date == null) {
            date = this.date;
        }

        if (duration == null) {
            duration = this.duration;
        }

        if (subject == null) {
            subject = this.subject;
        }

        return new StudySession(id, date, duration, subject);
    }

    @Override
    public String toString() {

        return String.format("ID: %d - Date: %s - Duration: %s - Subject: %s",
                id, date.toLocalDate().toString(), DurationFormatter.formatDuration(duration), subject);
    }

    public String toCSV() {
        StringBuilder builder = new StringBuilder();
        builder.append(id);
        builder.append(",");
        builder.append(date.toString());
        builder.append(",");
        builder.append(duration.toString());
        builder.append(",");
        builder.append(subject);
        return builder.toString();
    }
    public static StudySession fromCSV(String csv) {
        String[] arr = csv.split(",");
        if (arr.length != 4) {
            return null;
        }

        long id;
        LocalDateTime date;
        Duration duration;
        String subject;

        try {
            id = Long.parseLong(arr[0]);
            date = LocalDateTime.parse(arr[1]);
            duration = Duration.parse(arr[2]);
            subject = arr[3];
        } catch (NumberFormatException | DateTimeParseException e) {
            return null;
        }

        return new StudySession(id, date, duration, subject);
    }
}

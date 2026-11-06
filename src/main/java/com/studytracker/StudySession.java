package com.studytracker;

import java.sql.Date;
import java.time.Duration;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;

public class StudySession {

    public final long id;
    public final LocalDate date;
    public final Duration duration;
    public final String subject;
    public StudySession(long id, LocalDate date, Duration duration, String subject) {
        this.id = id;
        this.date = date;
        this.duration = duration;
        this.subject = subject;
    }

    @Override
    public String toString() {
        long hours = duration.toHours();
        int minutes = duration.toMinutesPart();
        int seconds = duration.toSecondsPart();

        return String.format("ID: %d - Date: %s - Duration: %02d:%02d:%02d - Subject: %s",
                id, date.toString(), hours, minutes, seconds, subject);
    }

    public String toCSV() {
        StringBuilder builder = new StringBuilder();
        builder.append(id);
        builder.append(",");
        builder.append(date.toString());
        builder.append(",");
        builder.append(duration);
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
        LocalDate date;
        Duration duration;
        String subject;

        try {
            id = Long.parseLong(arr[0]);
            date = LocalDate.parse(arr[1]);
            duration = Duration.parse(arr[2]);
            subject = arr[3];
        } catch (NumberFormatException | DateTimeParseException e) {
            return null;
        }

        return new StudySession(id, date, duration, subject);
    }


}

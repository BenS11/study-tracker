package com.studytracker.Study;

import com.studytracker.Interfaces.CSVable;
import com.studytracker.Utility.DurationFormatter;
import com.studytracker.Utility.StudyLogger;

import java.time.Duration;
import java.time.LocalDateTime;

public class ActiveSession implements CSVable {
    public static final String ACTIVE_SESSION_TOKEN = "ActiveSession";
    public final LocalDateTime startTime;
    public final String subject;
    private boolean completed = false;

    public ActiveSession(LocalDateTime startTime,String subject) {
        this.startTime = startTime;
        this.subject = subject;
    }

    public StudySession complete(long id) {
        Duration duration = Duration.between(startTime, LocalDateTime.now());
        completed = true;
        return new StudySession(id, startTime, duration, subject);
    }

    public boolean isCompleted() {
        return completed;
    }

    public String toCSV() {
        return ACTIVE_SESSION_TOKEN + "," + startTime + "," + subject;
    }

    @Override
    public String toString() {

        Duration duration = Duration.between(startTime, LocalDateTime.now());

        return String.format("Date: %s - Duration: %s - Subject: %s",
                startTime.toLocalDate().toString(), DurationFormatter.formatDuration(duration), subject);
    }

    public static ActiveSession fromCSV(String csv) {
        String[] arr = csv.split(",");

        try {
            LocalDateTime startTime = LocalDateTime.parse(arr[1]);

            return new ActiveSession(startTime, arr[2]);
        } catch (Exception e) {
            StudyLogger.logException(e);
        }

        return null;
    }
}

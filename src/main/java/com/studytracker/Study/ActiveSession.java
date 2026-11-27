package com.studytracker.Study;

import com.studytracker.Interfaces.CSVable;
import com.studytracker.Utility.DurationFormatter;
import com.studytracker.Utility.StudyLogger;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

public class ActiveSession implements CSVable {
    public static final String ACTIVE_SESSION_TOKEN = "ActiveSession";
    public final LocalDateTime startTime;
    public final String subject;
    private boolean completed = false;

    /**
     * Constructs an immutable Active Session object
     * @param startTime start time of the session
     * @param subject Subject being studied
     */
    public ActiveSession(LocalDateTime startTime,String subject) {
        this.startTime = startTime;
        this.subject = subject;
    }

    /**
     * Completes the active session, returning a full {@link StudySession}
     * @param id the id of the new session
     * @return {@link StudySession}
     */
    public StudySession complete(long id) {
        Duration duration = Duration.between(startTime, LocalDateTime.now());
        completed = true;
        return new StudySession(id, startTime, duration, subject);
    }

    /**
     * Whether the session is completed
     * @return a boolean
     */
    public boolean isCompleted() {
        return completed;
    }

    /**
     * {@link CSVable}
     * @return a CSV representation of this object
     */
    public String toCSV() {
        return ACTIVE_SESSION_TOKEN + "," + startTime + "," + subject;
    }

    /**
     * {@link Object#toString()}
     * @return a readable representation of this object
     */
    @Override
    public String toString() {

        Duration duration = Duration.between(startTime, LocalDateTime.now());

        return String.format("Date: %s - Duration: %s - Subject: %s",
                startTime.toLocalDate().toString(), DurationFormatter.formatDuration(duration), subject);
    }

    /**
     * Creates an ActiveSession from the CSV
     * @param csv a csv
     * @return an ActiveSession
     */
    public static ActiveSession fromCSV(String csv) throws DateTimeParseException {
        String[] arr = csv.split(",");


        LocalDateTime startTime = LocalDateTime.parse(arr[1]);

        return new ActiveSession(startTime, arr[2]);
    }
}

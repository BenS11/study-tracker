package com.studytracker.Study;

import com.studytracker.Interfaces.CSVable;
import com.studytracker.Utility.DurationFormatter;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeParseException;

/**
 * An immutable object containing details about a StudySession
 */
public class StudySession implements CSVable {
    public static final String DEFAULT_SUBJECT = "Unspecified";

    public final long id;
    public final LocalDateTime date;
    public final Duration duration;
    public final String subject;

    /**
     * Constructs a StudySession object
     * @param id ID (> 0) of the session - generally obtained using the study repository class
     * @param date The start time of the session
     * @param duration The duration of the session
     * @param subject The subject studied - default should be {@value DEFAULT_SUBJECT}
     */
    public StudySession(long id, LocalDateTime date, Duration duration, String subject) {
        this.id = id;
        this.date = date;
        this.duration = duration;
        this.subject = subject;
    }

    /**
     * Constructs a StudySession object
     * @param id ID (> 0) of the session - generally obtained using the study repository class
     * @param startTime start time of the session
     * @param endTime end time of the session
     * @param subject The subject studied - default should be {@value DEFAULT_SUBJECT}
     */
    public StudySession(long id, LocalDateTime startTime, LocalDateTime endTime, String subject) {
        this(id, startTime, Duration.between(startTime, endTime), subject);
    }

    /**
     * Constructs a new study object based on this one
     * Null copies the current field from this object
     * @param id The new id - likely obtained from a {@link StudyRepository}
     * @param date The new date (or null)
     * @param duration The new duration (or null)
     * @param subject The new subject (or null)
     * @return a new study object
     */
    public StudySession edit(long id, LocalDateTime date, Duration duration, String subject) {
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

    /**
     * Constructs a new study object based on this one.
     * Null parameter copies the current field from this object.
     * ID is taken from this object, so both objects should not be stored to prevent ID collision
     * @param date The new date (or null)
     * @param duration The new duration (or null)
     * @param subject The new subject (or null)
     * @return a new study object
     */
    public StudySession edit(LocalDateTime date, Duration duration, String subject) {

        return this.edit(id, date, duration, subject);
    }

    /**
     * {@link Object#toString()}
     * @return readable string representation of the Study Session
     */
    @Override
    public String toString() {

        String[] arr = date.toString().split("T");
        String dateStr;
        if (arr.length > 1) {
            arr[1] = arr[1].substring(0, 5);
            dateStr = arr[0] + " " + arr[1];
        } else {
            dateStr = date.toString();
        }


        return String.format("ID: %d - Date: %s - Duration: %s - Subject: %s",
                id, dateStr, DurationFormatter.formatDuration(duration), subject);
    }

    /**
     * {@link CSVable}
     * @return A csv that can be converted back into this object using {@link #fromCSV(String)}
     */
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

    /**
     * Converts a StudySession csv back into an object
     * @param csv the StudySession string obtained from {@link #toCSV()}
     * @return the corresponding StudySession
     */
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

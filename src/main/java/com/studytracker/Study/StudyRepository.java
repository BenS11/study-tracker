package com.studytracker.Study;

import com.studytracker.Utility.StudyLogger;

import java.io.*;
import java.nio.file.Path;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StudyRepository {
    private final ArrayList<StudySession> completeSessions;
    private final ArrayList<ActiveSession> activeSessions;

    private final Path finishedPath;
    private final Path activePath;

    public StudyRepository(Path finishedPath, Path activePath) {
        this.finishedPath = finishedPath;
        this.activePath = activePath;

        completeSessions = StudyIO.readAllCompleteSessions();
        activeSessions = StudyIO.readAllActiveSessions();
        sortSessions();
        Runtime.getRuntime().addShutdownHook(new Thread(this::flush));
    }



    public String addSession(StudySession s) {
        completeSessions.add(s);
        sortSessions();
        return "Wrote new StudySession: " + s.toString();
    }



    public StudySession readSession(long id) {
        for (StudySession session : completeSessions) {
            if (session.id == id) {
                return session;
            }
        }

        return null;
    }

    public String startSession(String subject) {
        return startSession(subject, LocalDateTime.now());
    }

    public String startSession(String subject, LocalDateTime startTime) {
        activeSessions.add(new ActiveSession(startTime, subject));
        sortSessions();
        return "Wrote start of session";
    }

    /**
     * Ends the oldest active session
     */
    public String endOldestSession() {
        if (activeSessions.isEmpty()) {
            return "No Active Session";
        }


        completeSessions.add(activeSessions.removeFirst().complete(getNextID()));
        return "Finished Session: " + completeSessions.getLast().toString();
    }


    public String endSessionBySubject(String subject) {
        return endSessionBySubjectAndStartTime(subject, null);
    }

    public String endSessionByStartTime(LocalDateTime startTime) {
        return endSessionBySubjectAndStartTime(null, startTime);
    }

    public String endSessionBySubjectAndStartTime(String subject, LocalDateTime startTime) {
        List<ActiveSession> list = activeSessions.stream()
                .filter(session -> subject == null || session.subject.equals(subject))
                .filter(session -> startTime == null || session.startTime.equals(startTime))
                .toList();

        if (list.size() > 1) {
            ActiveSession oldest = list.getFirst();
            StudySession s = oldest.complete(getNextID());

            activeSessions.remove(oldest);
            addSession(s);

            return "Multiple active sessions matched, ended oldest";
        }

        if (list.size() < 1) {
            return "No Sessions Matched";
        }

        ActiveSession as = list.getFirst();
        StudySession s = as.complete(getNextID());

        activeSessions.remove(as);
        addSession(s);
        return "Found and ended session: " + s.toString();
    }

    public StudySession readMostRecentSession() {
        if (completeSessions.isEmpty()) {
            return null;
        }

        return completeSessions.getLast();
    }

    /**
     * Sorts the sessions oldest to newest
     */
    private void sortSessions() {
        completeSessions.sort(
                Comparator.comparing((StudySession s) -> s.date)
        );

        activeSessions.sort(
                Comparator.comparing((ActiveSession s) -> s.startTime)
        );
    }

    public String displayActiveSessions() {
        if (activeSessions.isEmpty()) {
            return "No active sessions.";
        }

        StringBuilder sb = new StringBuilder("Active Sessions:\n");

        for (ActiveSession session : activeSessions) {
            sb.append(session).append("\n");
        }

        return sb.toString();
    }

    public String displayCompleteSessions() {
        if (completeSessions.isEmpty()) {
            return "No completed sessions.";
        }

        StringBuilder sb = new StringBuilder("Completed Sessions:\n");

        for (StudySession session : completeSessions) {
            sb.append(session).append("\n");
        }

        return sb.toString();
    }

    public String displayActiveSessions(LocalDate start, LocalDate end) {
        List<ActiveSession> filtered = activeSessions.stream()
                .filter(s -> (start == null || !s.startTime.toLocalDate().isBefore(start))
                        && (end == null || !s.startTime.toLocalDate().isAfter(end)))
                .toList();

        if (filtered.isEmpty()) return "No active sessions found.";

        StringBuilder sb = new StringBuilder("Active Sessions:\n");

        for (ActiveSession session : filtered) {
            sb.append(session).append("\n");
        }

        return sb.toString();
    }

    public String displayCompleteSessions(LocalDate start, LocalDate end) {
        List<StudySession> filtered = completeSessions.stream()
                .filter(s -> (start == null || !s.date.toLocalDate().isBefore(start))
                        && (end == null || !s.date.toLocalDate().isAfter(end)))
                .toList();

        if (filtered.isEmpty()) return "No completed sessions in this range.";

        StringBuilder sb = new StringBuilder("Completed Sessions:\n");

        for (StudySession session : filtered) {
            sb.append(session).append("\n");
        }

        return sb.toString();
    }

    /**
     * Writes all data on shutdown
     */
    public void flush() {
        try {
            StudyIO.writeCSVs(finishedPath.toFile(), completeSessions, false);
            StudyIO.writeCSVs(activePath.toFile(), activeSessions, false);
        } catch (IOException e) {
            StudyLogger.logException(e);
        }
    }

    /**
     * Clears all in memory sessions
     */
    public void wipe() throws IOException {
        activeSessions.clear();
        completeSessions.clear();
        StudyIO.wipeFile(activePath);
        StudyIO.wipeFile(finishedPath);
    }

    /**
     * Gets the next eligible id. Minimum value of 1
     * @return the next id
     */
    public long getNextID() {
        return completeSessions.stream()
                .mapToLong(s -> s.id)
                .max()
                .orElse(0) + 1;
    }

    public String deleteSession(long id) {
        if (completeSessions.removeIf(s -> s.id == id)) {
            return "Deleted session " + id;
        }

        return "Could not find session with id: " + id;
    }
}

package com.studytracker.Study;

import com.studytracker.Utility.StudyLogger;

import java.io.*;
import java.nio.file.Path;
import java.time.Duration;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

public class StudyRepository {
    private final ArrayList<StudySession> completeSessions;
    private final ArrayList<ActiveSession> activeSessions;

    private final Path completePath;
    private final Path activePath;

    public StudyRepository(Path finishedPath, Path activePath) {
        this.completePath = finishedPath;
        this.activePath = activePath;

        completeSessions = new ArrayList<>();
        activeSessions = new ArrayList<>();

        refreshFromFiles();
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
        return startSession(new ActiveSession(startTime, subject));
    }

    public String startSession(ActiveSession session) {
        activeSessions.add(session);
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
        List<ActiveSession> sessions = getActiveInRange(start, end);

        if (sessions.isEmpty()) return "No active sessions found.";

        StringBuilder sb = new StringBuilder("Active Sessions:\n");

        for (ActiveSession session : sessions) {
            sb.append(session).append("\n");
        }

        return sb.toString();
    }

    public String displayCompleteSessions(LocalDate start, LocalDate end) {
        List<StudySession> sessions = getCompletedInRange(start, end);

        if (sessions.isEmpty()) return "No completed sessions in this range.";

        StringBuilder sb = new StringBuilder("Completed Sessions:\n");

        for (StudySession session : sessions) {
            sb.append(session).append("\n");
        }

        return sb.toString();
    }

    public List<StudySession> getCompleted() {
        return List.copyOf(completeSessions);
    }

    public List<ActiveSession> getActive() {
        return List.copyOf(activeSessions);
    }

    public List<StudySession> getCompletedInRange(LocalDate start, LocalDate end) {
        return completeSessions.stream()
                .filter(s -> (start == null || !s.date.toLocalDate().isBefore(start))
                        && (end == null || !s.date.toLocalDate().isAfter(end)))
                .toList();
    }

    public List<ActiveSession> getActiveInRange(LocalDate start, LocalDate end) {
        return activeSessions.stream()
                .filter(s -> (start == null || !s.startTime.toLocalDate().isBefore(start))
                        && (end == null || !s.startTime.toLocalDate().isAfter(end)))
                .toList();
    }


    public int getStreak(String subject) {
        if (completeSessions.isEmpty()) {
            return 0;
        }
        // sorted oldest to newest, go newest to oldest for simplicity
        List<LocalDate> dateList = completeSessions
                .stream()
                .filter(s -> subject == null || s.subject.equals(subject))
                .map(s -> s.date.toLocalDate())
                .distinct()
                .toList()
                .reversed();

        int streak = 0;

        LocalDate current = LocalDate.now();
        for (LocalDate date : dateList) {
            if (date.equals(current)) {
                streak++;
            } else {
                break;
            }

            current = current.minusDays(1L);
        }

        return streak;
    }

    public boolean edit(long sessionId, LocalDateTime newDateTime, Duration newDuration, String newSubject) {
        StudySession session = null;

        for (StudySession s : completeSessions) {
            if (s.id == sessionId) {
                session = s;
            }
        }

        if (session == null) return false;

        completeSessions.remove(session);
        addSession(session.edit(newDateTime, newDuration, newSubject));
        return true;
    }

    public boolean isEmpty() {
        return completeSessions.isEmpty() && activeSessions.isEmpty();
    }

    public int size() {
        return completeSessions.size() + activeSessions.size();
    }

    /**
     * Writes all data on shutdown
     */
    public void flush() {
        try {
            StudyIO.writeCSVs(completePath.toFile(), completeSessions, false);
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
        StudyIO.wipeFile(completePath);
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

    public Path getCompletePath() {
        return completePath;
    }

    public Path getActivePath() {
        return activePath;
    }

    /**
     * Refreshes data from files - wipes all active data, use flush to put all active data in the files
     */
    public void refreshFromFiles() {
        completeSessions.clear();
        activeSessions.clear();
        completeSessions.addAll(StudyIO.readAllCompleteSessions(completePath));
        activeSessions.addAll(StudyIO.readAllActiveSessions(activePath));
        sortSessions();
    }
}

package com.studytracker.Study;

import com.studytracker.Utility.StudyLogger;

import java.io.*;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.Comparator;

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
        activeSessions.add(new ActiveSession(subject));
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
    public void wipe() {
        activeSessions.clear();
        completeSessions.clear();
    }

    public long getNextID() {
        return completeSessions.stream()
                .mapToLong(s -> s.id)
                .max()
                .orElse(0) + 1;
    }
}

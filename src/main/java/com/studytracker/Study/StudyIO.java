package com.studytracker.Study;

import com.studytracker.Interfaces.CSVable;
import com.studytracker.Utility.StudyLogger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.List;

public class StudyIO {

    private static final String START_SESSION_TOKEN = "Started,";

    private StudyIO() {}

    public static void writeCSVs(File dataFile, List<? extends CSVable> objs, boolean append) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(dataFile, append))) {
            for (CSVable obj : objs) {
                writer.println(obj.toCSV());
            }
        }
    }

    public static ArrayList<StudySession> readAllCompleteSessions() {
        ArrayList<StudySession> arr = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(StudyConfig.getGlobalConfig().getDataPath().toFile()))) {
            String csv;
            while ((csv = br.readLine()) != null) {
                if (csv.startsWith(START_SESSION_TOKEN)) {
                    continue;
                }

                StudySession s = StudySession.fromCSV(csv);
                if (s == null) {
                    continue;
                }
                arr.add(s);
            }
        } catch (IOException e) {
            StudyLogger.logException(e);
        }


        return arr;
    }

    public static ArrayList<ActiveSession> readAllActiveSessions() {
        ArrayList<ActiveSession> arr = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(StudyConfig.getGlobalConfig().getActiveSessionsDataPath().toFile()))) {
            String csv;
            while ((csv = br.readLine()) != null) {
                if (csv.startsWith(START_SESSION_TOKEN)) {
                    continue;
                }

                ActiveSession s = ActiveSession.fromCSV(csv);
                if (s == null) {
                    continue;
                }
                arr.add(s);
            }
        } catch (IOException e) {
            StudyLogger.logException(e);
        }


        return arr;
    }

    public static void wipeFile(Path path) throws IOException{
        Files.newBufferedWriter(
            path, StandardOpenOption.TRUNCATE_EXISTING).close();
    }
}

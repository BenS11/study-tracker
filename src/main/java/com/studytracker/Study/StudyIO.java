package com.studytracker.Study;

import com.studytracker.Interfaces.CSVable;
import com.studytracker.Utility.StudyLogger;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;

/**
 * A class which reads and writes from the paths
 */
public class StudyIO {


    private static final String START_SESSION_TOKEN = "Started,";

    // no instances
    private StudyIO() {}

    /**
     * Writes csvs to the path
     * @param dataFile The file to write
     * @param objs The data to write
     * @param append Whether to append or overwrite the file
     * @throws IOException If the operation fails
     */
    public static void writeCSVs(File dataFile, List<? extends CSVable> objs, boolean append) throws IOException {
        try (PrintWriter writer = new PrintWriter(new FileWriter(dataFile, append))) {
            for (CSVable obj : objs) {
                writer.println(obj.toCSV());
            }
        } catch (IOException e) {
            StudyLogger.logException(e);
            throw e;
        }
    }

    /**
     * Reads complete sessions
     * @param completePath The path of the completed sessions data ({@link StudyConfig#getCompletedDataPath()})
     * @return
     */
    public static ArrayList<StudySession> readAllCompleteSessions(Path completePath) {
        ArrayList<StudySession> arr = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(completePath.toFile()))) {
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

    /**
     * Read all complete sessions from the default path
     * @return a list
     */
    public static ArrayList<StudySession> readAllCompleteSessions() {
        return readAllCompleteSessions(StudyConfig.getGlobalConfig().getCompletedDataPath());
    }


    /**
     * Reads all active sessions from the path
     * @param activePath The path
     * @return a list
     */
    public static ArrayList<ActiveSession> readAllActiveSessions(Path activePath) {
        ArrayList<ActiveSession> arr = new ArrayList<>();
        try (BufferedReader br = new BufferedReader(new FileReader(activePath.toFile()))) {
            String csv;
            while ((csv = br.readLine()) != null) {
                if (csv.startsWith(START_SESSION_TOKEN)) {
                    continue;
                }
                try {
                    ActiveSession s = ActiveSession.fromCSV(csv);
                    arr.add(s);
                } catch (DateTimeParseException e) {
                    // unparsable
                    StudyLogger.logException(e);
                }
            }
        } catch (IOException e) {
            StudyLogger.logException(e);
        }


        return arr;
    }

    /**
     * Reads all active sessions from the default path
     * @return a list
     */
    public static ArrayList<ActiveSession> readAllActiveSessions() {
        return readAllActiveSessions(StudyConfig.getGlobalConfig().getActiveSessionsDataPath());
    }

    /**
     * Wipes the file, leaving it existent but empty
     * @param path the path of the file to wipe
     * @throws IOException if the operation fails
     */
    public static void wipeFile(Path path) throws IOException{
        Files.newBufferedWriter(
            path, StandardOpenOption.TRUNCATE_EXISTING).close();
    }
}

package com.studytracker.Study;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;


public class StudyConfig {
    private final Path config;
    private final Path data;
    private final Path activeSessionsData;
    private final Path logs;
    private final Properties props;

    public static final String DATA_FILENAME = "data.filename";
    public static final String ACTIVE_DATA_FILENAME = "activeSessions.filename";
    private static final String DATA_DIRECTORY_NAME = "data.directory";

    private static final StudyConfig globalConfig;

    static {
        try {
            globalConfig = new StudyConfig();
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }


    public static StudyConfig getGlobalConfig() {
        return globalConfig;
    }



    public StudyConfig() throws IOException {
        this(Path.of(System.getProperty("user.home"), ".study-tracker", "config.txt"));
    }

    public StudyConfig(Path configPath) throws IOException {
        this.config = configPath;

        ensureFileExists(config);

        this.props = new Properties();
        try (FileReader fr = new FileReader(config.toFile())) {
            props.load(fr);
        }

        String parentStr = configPath.toAbsolutePath().getParent().toString();

        String dataPath = getAndSetProperty(DATA_DIRECTORY_NAME, parentStr);
        String dataFile = getAndSetProperty(DATA_FILENAME, "data.csv");
        String activeFile = getAndSetProperty(ACTIVE_DATA_FILENAME, "active.csv");

        this.logs = Path.of(parentStr, "logs");
        ensureDirectoryExists(logs);

        Path dataDirectory = Path.of(dataPath);

        if (!dataDirectory.isAbsolute()) {
            dataDirectory = config.toAbsolutePath()
                    .getParent()
                    .resolve(dataDirectory);
        }

        this.data = dataDirectory.resolve(dataFile);
        ensureFileExists(data);



        this.activeSessionsData = dataDirectory.resolve(activeFile);
        ensureFileExists(activeSessionsData);

        writeConfig();
    }

    private void ensureFileExists(Path path) throws IOException {
        if (Files.isRegularFile(path)) {
            return;
        }

        Path parent = path.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.createFile(path);
    }

    private void ensureDirectoryExists(Path path) throws IOException {
        if (Files.exists(path)) {
            if (!Files.isDirectory(path)) {
                throw new IOException("Not a directory: " + path);
            }
            return;
        }

        Files.createDirectories(path);
    }

    private String getAndSetProperty(String fieldName, String defaultValue) {
        String ret = props.getProperty(fieldName);

        if (ret == null) {
            ret = defaultValue;
            props.setProperty(fieldName, defaultValue);
        }
        return ret;
    }



    public void writeConfig() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(config.toFile())){
            props.store(fos, "study-tracker configuration");
        }
    }

    public Path getDataPath() {
        return data;
    }

    public Path getConfigPath() {
        return config;
    }

    public Path getActiveSessionsDataPath() {
        return activeSessionsData;
    }

    public Path getLogsDirectory() { return logs; }

}

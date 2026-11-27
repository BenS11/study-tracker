package com.studytracker.Study;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;


/**
 * The configuration file for Study Tracker
 */
public class StudyConfig {
    // various paths
    private final Path config;
    private final Path completedData;
    private final Path activeSessionsData;
    private final Path logs;

    // Properties is a java class used to store a set of properties, has easy fileIO
    private final Properties props;

    public static final String DATA_FILENAME = "data.filename";
    public static final String ACTIVE_DATA_FILENAME = "activeSessions.filename";
    private static final String DATA_DIRECTORY_NAME = "data.directory";

    // a singleton instance
    private static final StudyConfig globalConfig;

    static {
        try {
            // Pass a path here if a non-default config path is preferred
            globalConfig = new StudyConfig();
        } catch (IOException e) {
            throw new ExceptionInInitializerError(e);
        }
    }


    /**
     * gets the program wide singleton instance of the config
     * @return THE StudyConfig
     */
    public static StudyConfig getGlobalConfig() {
        return globalConfig;
    }


    /**
     * Creates the default config
     * @throws IOException e
     */
    public StudyConfig() throws IOException {
        this(Path.of(System.getProperty("user.home"), ".study-tracker", "config.txt"));
    }

    /**
     * Creates a config at the path specified
     * @param configPath the preferred path of the config
     * @throws IOException IOException
     */
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

        this.completedData = dataDirectory.resolve(dataFile);
        ensureFileExists(completedData);



        this.activeSessionsData = dataDirectory.resolve(activeFile);
        ensureFileExists(activeSessionsData);

        writeConfig();
    }

    /**
     * Ensures the FILE exists, creating it if it doesn't. For directories, use {@link #ensureDirectoryExists(Path)}
     * @param path The exact path of the file
     * @throws IOException If the operation fails
     */
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

    /**
     * Ensures the DIRECTORY exists, creating it if it doesn't. For files, use {@link #ensureFileExists(Path)}
     * @param path The exact path of the file
     * @throws IOException If the operation fails or if the path is not a directory
     */
    private void ensureDirectoryExists(Path path) throws IOException {
        if (Files.exists(path)) {
            if (!Files.isDirectory(path)) {
                throw new IOException("Not a directory: " + path);
            }
            return;
        }

        Files.createDirectories(path);
    }

    /**
     * Gets the property, or sets it if it does not exist already
     * @param fieldName The name of the property
     * @param defaultValue The default value if it does not exist
     * @return the property
     */
    private String getAndSetProperty(String fieldName, String defaultValue) {
        String ret = props.getProperty(fieldName);

        if (ret == null) {
            ret = defaultValue;
            props.setProperty(fieldName, defaultValue);
        }
        return ret;
    }


    /**
     * Saves the config
     * @throws IOException if saving fails
     */
    public void writeConfig() throws IOException {
        try (FileOutputStream fos = new FileOutputStream(config.toFile())){
            props.store(fos, "study-tracker configuration");
        }
    }

    /**
     * Gets the path of the completed sessions data file
     * @return a path
     */
    public Path getCompletedDataPath() {
        return completedData;
    }

    /**
     * Gets the path of the config
     * @return a path
     */
    public Path getConfigPath() {
        return config;
    }

    /**
     * Gets the path of the active sessions data
     * @return a path
     */
    public Path getActiveSessionsDataPath() {
        return activeSessionsData;
    }

    /**
     * Gets the directory of the logs
     * @return a path
     */
    public Path getLogsDirectory() { return logs; }

}

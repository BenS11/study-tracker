package com.studytracker;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;


public class Config {
    private Path config;
    private Path data;
    private Properties props;

    public static final String DATA_FILENAME = "data.filename";
    private static final String DATA_DIRECTORY_NAME = "data.directory";




    public Config() throws IOException {
        this(Path.of(System.getProperty("user.home"), ".study-tracker", "config.txt"));
    }

    public Config(Path configPath) throws IOException {
        this.config = configPath;

        ensureFileExists(config);

        this.props = new Properties();
        try (FileReader fr = new FileReader(config.toFile())) {
            props.load(fr);
        }

        String parentStr = configPath.toAbsolutePath().getParent().toString();

        String dataPath = getAndSetProperty(DATA_DIRECTORY_NAME, parentStr);
        String dataFile = getAndSetProperty(DATA_FILENAME, "data.csv");

        this.data = Path.of(dataPath, dataFile);
        ensureFileExists(data);

        writeConfig();

    }

    private void ensureFileExists(Path path) throws IOException {
        if (Files.exists(path)) {
            return;
        }

        Path parent = path.getParent();

        if (parent != null) {
            Files.createDirectories(parent);
        }

        Files.createFile(path);
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
}

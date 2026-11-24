
package com.studytracker.Utility;

import com.studytracker.Study.StudyConfig;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.time.LocalDateTime;
import java.util.UUID;

public final class StudyLogger {

    private static Path logPath;

    private StudyLogger() {}

    private static synchronized Path getLogPath() throws IOException {
        if (logPath == null) {
            Path directory = StudyConfig.getGlobalConfig().getLogsDirectory();

            logPath = Files.createFile(
                    directory.resolve(
                            "log-" + UUID.randomUUID() + ".txt"
                    )
            );
        }

        return logPath;
    }

    public static synchronized void logException(Throwable e) {
        StringWriter trace = new StringWriter();
        e.printStackTrace(new PrintWriter(trace));

        String entry = String.format(
                "[%s] %s: %s%n%s%n",
                LocalDateTime.now(),
                e.getClass().getSimpleName(),
                e.getMessage(),
                trace
        );

        try {
            Files.writeString(
                    getLogPath(),
                    entry,
                    StandardOpenOption.APPEND
            );
        } catch (IOException | NullPointerException ex) {
            System.err.println("Failed to write application log:");
            ex.printStackTrace();
            e.printStackTrace();
        }
    }
}

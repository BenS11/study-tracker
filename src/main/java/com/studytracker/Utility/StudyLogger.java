
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

/**
 * A (somewhat incomplete) logger class.
 * This generally should not be used to swallow exceptions,
 * and should supplement other error handling.
 */
public final class StudyLogger {

    /**
     * Path of the log file
     */
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

    /**
     * Sets the logging path.
     * TODO: move away from a class with static state to a singleton or other method
     * @param newLogPath The path
     */
    public static synchronized void setLogPath(Path newLogPath) {
        logPath = newLogPath;
    }

    /**
     * A method for logging exceptions. This is synchronized to allow for logging from a gui thread or other thread in the future.
     * @param e Throwable exception
     */
    public static synchronized void logException(Throwable e) {
        String entry = String.format(
                "[%s] %s: %s",
                LocalDateTime.now(),
                e.getClass().getSimpleName(),
                e.getMessage()
        );

        try {
            Files.writeString(
                    getLogPath(),
                    entry,
                    StandardOpenOption.APPEND
            );
        } catch (IOException | NullPointerException ex) {
            System.err.println("Failed to write error log:");
            ex.printStackTrace(System.err);
            e.printStackTrace(System.err);
        }
    }
}

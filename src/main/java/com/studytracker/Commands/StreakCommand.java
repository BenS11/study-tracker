package com.studytracker.Commands;

import com.studytracker.Display.StudyUserIO;
import com.studytracker.Statistics.Statistics;
import picocli.CommandLine;

import java.time.LocalDate;

@CommandLine.Command(
        name = "summary",
        description = "summarizes logged data"
)
public class StreakCommand implements Runnable {
    private final Statistics stats;
    private final StudyUserIO disp;

    public StreakCommand(StudyUserIO disp, Statistics stats) {
        this.disp = disp;
        this.stats = stats;
    }

    @CommandLine.Option(
            names = {"-s", "--subject"},
            description = "Streak by session"
    )
    private String subject;
    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        disp.println(stats.streak(subject));
    }
}

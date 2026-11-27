package com.studytracker.Commands;

import com.studytracker.Display.StudyUserIO;
import com.studytracker.Statistics.Statistics;
import picocli.CommandLine;

import java.time.LocalDate;

@CommandLine.Command(
        name = "summary",
        description = "summarizes logged data"
)
public class SummaryCommand implements Runnable {
    private final Statistics stats;
    private final StudyUserIO disp;

    /**
     * Constructs a Summary command - displays the user's streak
     * @param display The user's display
     * @param stats The statistics service
     */
    public SummaryCommand(StudyUserIO display, Statistics stats) {
        this.disp = display;
        this.stats = stats;
    }

    @CommandLine.Option(
            names = {"-s", "--start-date"},
            description = "The start date of the summary"
    )
    private LocalDate start;

    @CommandLine.Option(
            names = {"-e", "--end-date"},
            description = "The start date of the summary"
    )
    private LocalDate end;

    @CommandLine.Option(
            names = {"-a", "--all"},
            description = "Summarize all sessions"
    )
    private boolean all;

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        end = end == null ? LocalDate.now() : end;
        if (start == null) {
            start = all ? LocalDate.EPOCH : end.minusWeeks(1L);
        }

        disp.println(stats.summary(start, end));
    }
}

package com.studytracker.Commands;

import com.studytracker.Display.StudyUserIO;
import com.studytracker.Study.StudyRepository;
import picocli.CommandLine;

import java.time.LocalDate;

@CommandLine.Command(
        name = "display",
        description = "Displays study sessions"
)
public class DisplayCommand implements Runnable {

    private final StudyRepository repo;
    private final StudyUserIO display;

    @CommandLine.Option(
            names = {"-a", "--active"},
            description = "Display only active sessions"
    )
    private boolean active;

    @CommandLine.Option(
            names = {"-c", "--completed"},
            description = "Display only completed sessions (default)"
    )
    private boolean completed;


    @CommandLine.Option(
            names = {"-s", "--start-range"},
            description = " Date of start of range to filter (yyyy-MM-dd)"
    )
    private LocalDate startRange;

    @CommandLine.Option(
            names = {"-e", "--end-range"},
            description = " Date of end of range to filter (yyyy-MM-dd)"
    )
    private LocalDate endRange;

    public DisplayCommand(StudyRepository repo, StudyUserIO display) {
        this.repo = repo;
        this.display = display;
    }

    @Override
    public void run() {
        if (active) {
            if (startRange != null || endRange != null) {
                display.println(repo.displayActiveSessions(startRange, endRange));
            } else {
                display.println(repo.displayActiveSessions());
            }
        }

        // default behavior
        if (!active || completed) {
            if (startRange != null || endRange != null) {
                display.println(repo.displayCompleteSessions(startRange, endRange));
            } else {
                display.println(repo.displayCompleteSessions());
            }
        }
    }
}
package com.studytracker.Commands;

import com.studytracker.Study.StudyRepository;
import picocli.CommandLine;

import java.time.LocalDate;
import java.time.LocalTime;

@CommandLine.Command(
        name = "begin",
        description = "Begins a new study session."
)
public class BeginCommand implements Runnable {

    private final StudyRepository repo;

    @CommandLine.Parameters(
            index = "0..*",
            description = "Subject being studied"
    )
    private String[] subjectArgs = {};

    @CommandLine.Option(
            names = {"-d", "--date"},
            description = "Session start date (yyyy-MM-dd)"
    )
    private LocalDate date;

    @CommandLine.Option(
            names = {"-t", "--time"},
            description = "Session start time (HH:mm)"
    )
    private LocalTime time;

    @CommandLine.Option(
            names = {"-n", "--notes"},
            description = "Notes about the session"
    )
    private String notes;

    public BeginCommand(StudyRepository repo) {
        this.repo = repo;
    }

    @Override
    public void run() {
        String subject = subjectArgs.length == 0
                ? "Unspecified"
                : String.join(" ", subjectArgs);

        // Default to the current date and time.
        LocalDate startDate = date != null
                ? date
                : LocalDate.now();

        LocalTime startTime = time != null
                ? time
                : LocalTime.now();

        System.out.println(
                repo.startSession(subject)
        );
    }
}
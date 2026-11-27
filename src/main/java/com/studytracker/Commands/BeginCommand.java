package com.studytracker.Commands;

import com.studytracker.Display.StudyUserIO;
import com.studytracker.Study.StudyRepository;
import picocli.CommandLine;

import java.time.LocalDate;
import java.time.LocalTime;


// this annotation helps picocli creates messages and documentation
@CommandLine.Command(
        name = "begin",
        description = "Begins a new study session."
)
public class BeginCommand implements Runnable {

    private final StudyRepository repo;
    private final StudyUserIO display;

    // this is positional to allow a subject like "Quantum Mechanics" to be parsed correctly
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

    // this is an option I plan to add in the future
    //    @CommandLine.Option(
    //            names = {"-n", "--notes"},
    //            description = "Notes about the session"
    //    )
    //    private String notes;

    /**
     * Constructs a begin command - begins a session
     * @param repo The users repository
     * @param display The user's display
     */
    public BeginCommand(StudyRepository repo, StudyUserIO display) {
        this.repo = repo;
        this.display = display;
    }


    @Override
    public void run() {
        String subject = subjectArgs.length == 0
                ? "Unspecified"
                : String.join(" ", subjectArgs);

        LocalDate startDate = date != null ? date : LocalDate.now();

        LocalTime startTime = time != null
                ? time
                : LocalTime.now();

        display.println(
                repo.startSession(subject)
        );
    }
}
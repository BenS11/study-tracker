package com.studytracker.Commands;

import com.studytracker.Display.StudyUserIO;
import com.studytracker.Study.StudyRepository;
import picocli.CommandLine;

import java.time.Duration;
import java.time.LocalDateTime;

@CommandLine.Command(
        name = "edit",
        description = "Edits a study session by id"
)
public class EditCommand implements Runnable {

    private final StudyRepository repo;
    private final StudyUserIO display;

    @CommandLine.Option(
            names = {"-i", "--id"},
            description = "The id of the session to edit"
    )
    private Long id;

    @CommandLine.Option(
            names = {"-st", "--start-date-time"},
            description = "Session start time (yyyy-MM-ddTHH:mm)"
    )
    private LocalDateTime startTime;

    @CommandLine.Option(
            names = {"-e", "--end-time"},
            description = "Session end time (yyyy-MM-ddTHH:mm)"
    )
    private LocalDateTime endTime;

    @CommandLine.Option(
            names = {"-s", "--subject"},
            description = "The new subject"
    )
    private String subject;

    /**
     * Constructs an edit command - edit sessions by id
     * @param repo The users repository
     * @param display The user's display
     */
    public EditCommand(StudyRepository repo, StudyUserIO display) {
        this.repo = repo;
        this.display = display;
    }

    @Override
    public void run() {
        Duration dur;
        if (startTime == null || endTime == null) {
            dur = null;
        } else {
            dur = Duration.between(startTime, endTime);
        }


        boolean success = repo.edit(id, startTime, dur, subject);

        if (success) {
            display.println("Edited session " + id);
        } else {
            display.println("Could not edit session " + id);
        }
    }
}

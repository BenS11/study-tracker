package com.studytracker.Commands;

import com.studytracker.Study.StudyRepository;
import com.studytracker.Display.StudyUserIO;
import picocli.CommandLine;

@CommandLine.Command(
        name = "delete",
        description = "Deletes a completed study session."
)
public class DeleteCommand implements Runnable {

    private final StudyRepository repo;
    private final StudyUserIO display;

    @CommandLine.Parameters(
            index = "0",
            paramLabel = "-id",
            description = "ID of the session to delete"
    )
    private long id;

    /**
     * Constructs a delete command - deletes sessions by id
     * @param repo The users repository
     * @param display The user's display
     */
    public DeleteCommand(StudyRepository repo, StudyUserIO display) {
        this.repo = repo;
        this.display = display;
    }

    @Override
    public void run() {
        display.println(repo.deleteSession(id));
    }
}
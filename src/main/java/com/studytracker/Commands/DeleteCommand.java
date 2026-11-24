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

    public DeleteCommand(StudyRepository repo, StudyUserIO display) {
        this.repo = repo;
        this.display = display;
    }

    @Override
    public void run() {
        display.println(repo.deleteSession(id));
    }
}
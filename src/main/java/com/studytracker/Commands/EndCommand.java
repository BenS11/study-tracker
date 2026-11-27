package com.studytracker.Commands;

import com.studytracker.Display.StudyUserIO;
import com.studytracker.Study.StudyRepository;
import picocli.CommandLine;

@CommandLine.Command(
        name = "end",
        description = "Ends an active study session."
)
public class EndCommand implements Runnable {

    private final StudyRepository repo;
    private final StudyUserIO display;


    @CommandLine.Option(
            names = {"-s", "--subject"},
            description = "Subject name"
    )
    private String subject;

    /**
     * Constructs an edit command - edit sessions by id
     * @param repo The users repository
     * @param display The user's display
     */
    public EndCommand(StudyRepository repo, StudyUserIO display) {
        this.repo = repo;
        this.display = display;
    }

    @Override
    public void run() {
        if (subject != null) {
            display.println(repo.endSessionBySubject(subject));
        } else {
            display.println(repo.endOldestSession());
        }

    }
}

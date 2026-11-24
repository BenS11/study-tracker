package com.studytracker.Commands;

import com.studytracker.Display.StudyUserIO;
import com.studytracker.Study.StudyRepository;
import picocli.CommandLine;

import java.io.IOException;

public class ResetCommand implements Runnable {


    private final StudyRepository repo;
    private final StudyUserIO disp;

    @CommandLine.Option(
            names = {"-f", "--force"},
            description = "force reset"
    )
    private boolean force;

    public ResetCommand(StudyRepository repo, StudyUserIO disp)
    {
        this.repo = repo;
        this.disp = disp;
    }

    /**
     * Executes a task
     */
    @Override
    public void run() {

        if (!force) {
            disp.println("Are you sure you want to wipe all data?");
            disp.println("Run reset -f if you are sure");
            return;
        }
        try {
            repo.wipe();
            disp.println("Wiped data.csv and active.csv");
        } catch (IOException e) {
            disp.println("Could not wipe data -IOException");
        }
    }

}

package com.studytracker.Commands;

import com.studytracker.Display.StudyUserIO;
import picocli.CommandLine;

@CommandLine.Command(
        name = "study",
        description = "Study tracker",
        subcommands = {}
)
public class RootCommand implements Runnable {
    private final StudyUserIO disp;

    public RootCommand(StudyUserIO disp) {
        this.disp = disp;
    }

    @Override
    public void run() {
        disp.println("Use 'study --help' for available commands.");
    }
}
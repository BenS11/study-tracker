package com.studytracker.Commands;

import com.studytracker.Display.StudyUserIO;
import picocli.CommandLine;

/**
 * {@link RootCommand#RootCommand(StudyUserIO)}  RootCommand}
 */
@CommandLine.Command(
        name = "study",
        description = "Study tracker",
        subcommands = {}
)
public class RootCommand implements Runnable {
    private final StudyUserIO disp;

    /**
     * Constructs the root command - the command that runs when only a single space is entered.
     * For more information, check out {@link CommandLine} and {@link CommandLine#addSubcommand(Object)}
     * @param disp The user's display
     */
    public RootCommand(StudyUserIO disp) {
        this.disp = disp;
    }

    @Override
    public void run() {
        disp.println("Use 'study --help' for available commands.");
    }
}
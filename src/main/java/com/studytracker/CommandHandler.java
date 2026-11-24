package com.studytracker;

import com.studytracker.Commands.*;
import com.studytracker.Display.StudyUserIO;
import com.studytracker.Study.StudyRepository;
import picocli.CommandLine;

public class CommandHandler {

    private final CommandLine cli;
    private final StudyUserIO display;
    private final StudyRepository repo;

    public CommandHandler(StudyRepository repo, StudyUserIO display) {
        this.display = display;
        this.repo = repo;

        cli = new CommandLine(new RootCommand(display));

        cli.addSubcommand("begin", new BeginCommand(repo, display));
        cli.addSubcommand("end", new EndCommand(repo, display));
        cli.addSubcommand("record", new RecordCommand(repo, display));
        cli.addSubcommand("display", new DisplayCommand(repo, display));
        cli.addSubcommand("delete", new DeleteCommand(repo, display));
        cli.addSubcommand("help", new CommandLine.HelpCommand());
        cli.addSubcommand("reset", new ResetCommand(repo, display));
    }

    public void run() {
        while (true) {
            String input = display.read();

            if (input.equalsIgnoreCase("exit")) {
                break;
            }

            if (input.isEmpty()) {
                continue;
            }

            // matches multiple spaces
            String[] args = input.split("\\s+");
            cli.execute(args);
        }
    }
}
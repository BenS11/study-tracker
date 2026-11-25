package com.studytracker;

import com.studytracker.Commands.*;
import com.studytracker.Display.StudyUserIO;
import com.studytracker.Statistics.Statistics;
import com.studytracker.Study.StudyRepository;
import picocli.CommandLine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class CommandHandler {

    private final CommandLine cli;
    private final StudyUserIO display;
    private boolean firstTime;

    public CommandHandler(StudyRepository repo, StudyUserIO display, Statistics stats) {
        this.display = display;

        cli = new CommandLine(new RootCommand(display));

        cli.registerConverter(
                LocalDateTime.class,
                value -> LocalDateTime.parse(
                        value,
                        DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm")
                )
        );

        firstTime = repo.isEmpty();

        cli.addSubcommand("begin", new BeginCommand(repo, display));
        cli.addSubcommand("end", new EndCommand(repo, display));
        cli.addSubcommand("record", new RecordCommand(repo, display));
        cli.addSubcommand("display", new DisplayCommand(repo, display));
        cli.addSubcommand("delete", new DeleteCommand(repo, display));
        cli.addSubcommand("help", new CommandLine.HelpCommand());
        cli.addSubcommand("streak", new StreakCommand(display, stats));
        cli.addSubcommand("summary", new SummaryCommand(display, stats));
        cli.addSubcommand("edit", new EditCommand(repo, display));
        cli.addSubcommand("reset", new ResetCommand(repo, display));
    }

    public void run() {
        display.println(getWelcomeMessage());

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

    public String getWelcomeMessage() {
        String message = "Welcome to Study Tracker!";
        if (firstTime) {
            message += "\n Type help to get started";
        }

        return message;
    }

    public void executeSingle(String[] args) {
        cli.execute(args);
    }
}
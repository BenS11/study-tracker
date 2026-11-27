package com.studytracker;

import com.studytracker.Commands.*;
import com.studytracker.Display.StudyUserIO;
import com.studytracker.Statistics.Statistics;
import com.studytracker.Study.StudyRepository;
import picocli.CommandLine;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

/**
 * The application handler
 */
public class CommandHandler {

    private final CommandLine cli;
    private final StudyUserIO display;
    private final boolean firstTime;

    /**
     * Constructs an application handler based on the repo, the UserIO, and the statistics service
     * @param repo The repository the user is using
     * @param display The user display
     */
    public CommandHandler(StudyRepository repo, StudyUserIO display) {
        this.display = display;
        Statistics stats = new Statistics(repo);

        // the command interpreter, this takes input and executes it.
        // this is basically the core of picocli
        cli = new CommandLine(new RootCommand(display));

        // register a way to parse a date time in a much more human-readable way
        // this makes it much easier to enter an exact time
        // the relevant symbols are:
        // y year-of-era year 2004; 04,
        // M month-of-year number 7; 07;
        // d day-of-month number 10
        // H hour-of-day (0-23) number 0
        // m minute-of-hour number 30
        // T -> placeholder (instead of a space)
        cli.registerConverter(
                LocalDateTime.class,
                value -> LocalDateTime.parse(
                        value,
                        DateTimeFormatter.ofPattern("yyyy-MM-ddTHH:mm")
                )
        );

        // basic assumption, could be more robust, possibly a config variable?
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
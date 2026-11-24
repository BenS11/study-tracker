package com.studytracker.Commands;


import com.studytracker.Study.StudyRepository;

public class CommandHandler {

    private final StudyRepository repo;

    public CommandHandler(StudyRepository repo) {
        this.repo = repo;
    }

    public Command parseCommand(String[] args) {
        if (args.length < 1) {
            return new NullCommand();
        }

        return switch (args[0]) {
            case "begin" -> new BeginCommand(repo);
            case "end" -> new EndCommand(repo);
            case "help" -> new HelpCommand();
            case "record" -> new RecordCommand();
            case "reset" -> new ResetCommand(repo);
            default -> new NullCommand();
        };
    }

}
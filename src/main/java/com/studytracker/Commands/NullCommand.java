package com.studytracker.Commands;

public class NullCommand implements Command {

    @Override
    public String execute(String arguments) {
        return "Command not recognized: " + arguments;
    }

    public static String getManualStatement() {
        return "Null Command - returned when a command is unrecognized";
    }
}

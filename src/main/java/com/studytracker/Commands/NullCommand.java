package com.studytracker.Commands;

import java.util.Arrays;

public class NullCommand implements Command {
    String message;
    
    public NullCommand() {}
    
    public NullCommand(String message) {
        this.message = message;
    }


    @Override
    public String execute(String[] arguments) {

        if (message == null) {
            return "Command not recognized, Args:" + Arrays.toString(arguments) + "\n" + "(use help for help)";
        }

        return message;
    }

    public String getManualPage() {
        return "Null Command - returned when a command is unrecognized";
    }
}

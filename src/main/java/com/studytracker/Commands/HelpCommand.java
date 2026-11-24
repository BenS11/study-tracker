package com.studytracker.Commands;

public class HelpCommand implements Command {
    @Override
    public String execute(String[] arguments) {
        return "Unimplimented";
    }
    
    public String getManualPage() {
        return "Gets the manual statement";
    }
}

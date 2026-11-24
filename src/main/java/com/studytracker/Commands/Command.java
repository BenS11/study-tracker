package com.studytracker.Commands;

public interface Command {

    /**
     * Executes a task, and returns a String representing
     * @return A message for the user
     */
    public String execute(String[] arguments);


    public String getManualPage();
}

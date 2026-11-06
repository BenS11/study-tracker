package com.studytracker;

public interface Command {

    /**
     * Executes a task, and returns a String representing
     * @param arguments
     * @return A message for the user
     */
    public String execute(String arguments);
}

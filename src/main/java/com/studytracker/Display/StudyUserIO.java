package com.studytracker.Display;

/**
 * Interface for abstracting out user input and output
 * This exists to allow GUI migration or other UI
 */
public interface StudyUserIO {
    /**
     * Displays a message to the user
     * @param message A message
     */
    public void print(String message);
    /**
     * Displays a message to the user and ends the line
     * @param message A message
     */
    public void println(String message);

    /**
     * Gets input from the user
     */
    public String read();

    /**
     * Gets input from the user
     * @param message A message to display
     */
    public String read(String message);
}

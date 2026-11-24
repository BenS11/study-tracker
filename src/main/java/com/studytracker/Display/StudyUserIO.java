package com.studytracker.Display;

/**
 * Interface for abstracting out user input and output
 * This exists to eventually allow GUI migration
 */
public interface StudyUserIO {
    public void print(String message);
    public void println(String message);
    public String read();
    public String read(String message);
}

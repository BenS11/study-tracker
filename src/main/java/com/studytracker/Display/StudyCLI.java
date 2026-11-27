package com.studytracker.Display;

/**
 * A Command Line Interface implementation of {@link StudyUserIO}
 */
public class StudyCLI implements StudyUserIO {

    public StudyCLI() {}

    @Override
    public void print(String message) {
        System.out.print(message);
    }

    @Override
    public void println(String message) {
        System.out.println(message);
    }

    @Override
    public String read() {
        return IO.readln("study>");
    }

    @Override
    public String read(String message) {
        return IO.readln(message);
    }

}

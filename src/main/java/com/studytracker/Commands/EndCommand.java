package com.studytracker.Commands;

import com.studytracker.Study.StudyRepository;

import java.util.Arrays;

public class EndCommand implements Command {

    private final StudyRepository repo;

    public EndCommand(StudyRepository repo) {
        this.repo = repo;
    }

    /**
     * Executes a task, and returns a String representing
     *
     * @return A message for the user
     */
    @Override
    public String execute(String[] arguments) {
        return repo.endOldestSession();
    }

    @Override
    public String getManualPage() {
        return "";
    }
}

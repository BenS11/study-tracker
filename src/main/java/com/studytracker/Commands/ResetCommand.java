package com.studytracker.Commands;

import com.studytracker.Study.StudyConfig;
import com.studytracker.Study.StudyRepository;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.StandardOpenOption;

public class ResetCommand implements Command {


    private final StudyRepository repo;

    public ResetCommand(StudyRepository repo) {
        this.repo = repo;
    }
    /**
     * Executes a task, and returns a String representing
     *
     * @return A message for the user
     */
    @Override
    public String execute(String[] arguments) {
        String checkOne = IO.readln("Are you sure you want to wipe all data? (y/n):\n").toLowerCase();
        if (!checkOne.equals("y")) {
            return "Aborted wipe";
        }

        String checkTwo = IO.readln("Are you really sure you want to wipe all data? (y/n):\n").toLowerCase();

        if (!checkTwo.equals("y")) {
            return "Aborted wipe";
        }

        repo.wipe();
        try {
            Files.newBufferedWriter(
                    StudyConfig.getGlobalConfig().getDataPath(),
                    StandardOpenOption.TRUNCATE_EXISTING).close();

            Files.newBufferedWriter(
                    StudyConfig.getGlobalConfig().getActiveSessionsDataPath(),
                    StandardOpenOption.TRUNCATE_EXISTING).close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        return "Wiped data.csv";
    }

    @Override
    public String getManualPage() {
        return "";
    }
}

package com.studytracker;

import com.studytracker.Display.StudyCLI;
import com.studytracker.Display.StudyUserIO;
import com.studytracker.Statistics.Statistics;
import com.studytracker.Study.StudyConfig;
import com.studytracker.Study.StudyRepository;

public class Main {
    public static void main(String[] args) {

        StudyRepository repo = new StudyRepository(
                StudyConfig.getGlobalConfig().getCompletedDataPath(),
                StudyConfig.getGlobalConfig().getActiveSessionsDataPath());
        StudyUserIO disp = new StudyCLI();
        CommandHandler handler = new CommandHandler(
                repo,
                disp
        );

        handler.run();
    }
}

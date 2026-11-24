package com.studytracker;

import com.studytracker.Display.StudyCLI;
import com.studytracker.Display.StudyUserIO;
import com.studytracker.Study.StudyConfig;
import com.studytracker.Study.StudyRepository;

public class Main {
    public static void main(String[] args) {

        StudyUserIO disp = new StudyCLI();

        CommandHandler handler = new CommandHandler(
                new StudyRepository(
                        StudyConfig.getGlobalConfig().getDataPath(),
                        StudyConfig.getGlobalConfig().getActiveSessionsDataPath()),
                disp
        );

        handler.run();
    }
}

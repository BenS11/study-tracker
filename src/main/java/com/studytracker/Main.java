package com.studytracker;

import java.util.Arrays;

import com.studytracker.Commands.Command;
import com.studytracker.Commands.CommandHandler;
import com.studytracker.Study.StudyConfig;
import com.studytracker.Study.StudyRepository;

public class Main {
    public static void main(String[] args) {
        if (StudyConfig.getGlobalConfig() == null
                || StudyConfig.getGlobalConfig().getConfigPath() == null
                || StudyConfig.getGlobalConfig().getDataPath() == null) {
            IO.println("Could not initialize config");

            return;
        }


        CommandHandler handler = new CommandHandler(
                new StudyRepository(
                        StudyConfig.getGlobalConfig().getDataPath(),
                        StudyConfig.getGlobalConfig().getActiveSessionsDataPath())
        );

        String[] input = IO.readln("Enter command or type exit to finish:\n").split(" ");
        do {

            Command c = handler.parseCommand(input);
            IO.println(c.execute(Arrays.copyOfRange(input, Math.min(input.length, 1), input.length)));

            input = IO.readln("Enter command or type exit to finish:\n").split(" ");
        } while (input.length > 0 && !input[0].equalsIgnoreCase("exit"));
            
    }
}

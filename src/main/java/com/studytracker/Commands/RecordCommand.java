package com.studytracker.Commands;

import com.studytracker.Display.StudyUserIO;
import com.studytracker.Study.StudyRepository;
import com.studytracker.Study.StudySession;
import picocli.CommandLine;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

@CommandLine.Command(
        name = "record",
        description = "records a study session"
)
public class RecordCommand implements Runnable {


    private final StudyUserIO display;
    private final StudyRepository repo;

    public RecordCommand(StudyRepository repo, StudyUserIO display) {
        this.display = display;
        this.repo = repo;
    }

    @CommandLine.Option(
            names = {"-d", "--date-time"},
            description = "The start time of the session - to begin a session use 'begin'"
    )
    LocalDateTime startTime;

    @CommandLine.Option(
            names = {"-t", "--time"},
            description = "Session start time (HH:mm)"
    )
    LocalTime todayTime;

    @CommandLine.Option(
            names = {"-e", "--end-time"},
            description = "The end time of the session - to end an active session use end"
    )
    LocalDateTime endTime;

    @CommandLine.Option(
            names = {"-s", "--subject"},
            description = "subject",
            defaultValue = "Unspecified"
    )
    String subject;

    /**
     * Runs this operation.
     */
    @Override
    public void run() {
        if (startTime == null && todayTime == null) {
            display.println("Must specify start time using --date-time or --time");
            return;
        }

        if (startTime == null) {
            startTime = LocalDateTime.of(LocalDate.now(), todayTime);
        }

        endTime =  endTime == null ? LocalDateTime.now() : endTime;

        StudySession session = new StudySession(repo.getNextID(), startTime, endTime, subject);
        display.println(repo.addSession(session));
    }
}

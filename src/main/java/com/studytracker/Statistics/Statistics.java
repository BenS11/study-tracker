package com.studytracker.Statistics;

import com.studytracker.Study.StudyRepository;
import com.studytracker.Study.StudySession;
import com.studytracker.Utility.DurationFormatter;

import java.time.Duration;
import java.time.LocalDate;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Statistics {
    private final StudyRepository repo;

    public Statistics(StudyRepository repo) {
        this.repo = repo;
    }


    public String summary(LocalDate start, LocalDate end) {
        List<StudySession> sessionList = repo.getCompletedInRange(start, end);

        long totalSeconds = sessionList
                .stream()
                .mapToLong(session -> session.duration.toSeconds())
                .sum();

        HashMap<String, Long> subjects = new HashMap<>();

        for (StudySession studySession : sessionList) {
            String subj = studySession.subject;
            subjects.put(subj,
                    subjects.getOrDefault(subj, 0L) + studySession.duration.toSeconds());
        }

        String mostStudied  = "";
        Long largest = -1L;

        for (Map.Entry<String, Long> entry : subjects.entrySet()) {
            if (entry.getValue() > largest) {
                largest = entry.getValue();
                mostStudied = entry.getKey();
            }
        }

        StringBuilder summary = new StringBuilder(
                "Summary: " + start.toString() + " - " + end.toString() + "\n");
        if (mostStudied.isEmpty()) {
            summary.append("Logged No Time! >:(\n");
            return summary.toString();
        }

        Duration dur = Duration.ofSeconds(totalSeconds);
        Duration mostStudiedDur = Duration.ofSeconds(largest);
        int days = start.until(end).getDays();
        summary.append("Total Time Spent Studying: ")
                .append(DurationFormatter.formatDuration(dur))
                .append("\n");
        summary.append("Average Time Spent Per Session: ")
                .append(DurationFormatter.formatDuration(dur.dividedBy(sessionList.size())))
                .append("\n");
        summary.append("Average Time Spent Per Active Day: ")
                .append(DurationFormatter.formatDuration(dur.dividedBy(days)))
                .append("\n");
        summary.append("Spent Most Time Studying: ")
                .append(mostStudied).append(" for: ")
                .append(DurationFormatter.formatDuration(mostStudiedDur)).append("\n");
        summary.append("Good Job!\n");

        return summary.toString();
    }

    public String streak(String subject) {
        int streak = repo.getStreak(subject);

        StringBuilder builder = new StringBuilder("Current Streak: ").append(streak).append("\n");
        if (streak > 10) {
            builder.append("Great Job!\n");
        } else if (streak > 5) {
            builder.append("Good Job!\n");
        } else if (streak > 0) {
            builder.append("Keep it going!\n");
        } else {
            builder.append("Uh oh...\n");
        }

        return builder.toString();
    }
}

package com.studytracker.Utility;

import com.studytracker.StudySession;

public class StudyIO {

    private StudyIO() {}

    public static void writeSession(StudySession s) {
        s.toCSV()
    }
}

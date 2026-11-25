package com.studytracker;

import com.studytracker.Study.ActiveSession;
import com.studytracker.Study.StudyRepository;
import com.studytracker.Study.StudySession;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;

import static org.junit.jupiter.api.Assertions.*;

import java.nio.file.Path;
import java.time.LocalDateTime;
import java.util.Random;


public class StudyRepositoryTest {
    @TempDir
    Path tempDir;

    StudyRepository emptyRepo;
    StudyRepository populatedRepo;

    private Random random;
    private long id;

    @BeforeEach
    public void setUp() {
        random = new Random(42L);
        id = 1L;

        emptyRepo = new StudyRepository(
                tempDir.resolve("cleanActiveData.csv"),
                tempDir.resolve("cleanCompleteData.csv"));

        // should be populated in tests
        populatedRepo = new StudyRepository(
                tempDir.resolve("populatedActiveData.csv"),
                tempDir.resolve("populatedCompleteData.csv"));
    }

    @Test
    public void testSize() {
        assertTrue(emptyRepo.isEmpty());
        assertTrue(populatedRepo.isEmpty());

        addSessions(5);

        assertEquals(5, populatedRepo.size());

        addActiveSessions(2);

        // counts both active and completed
        assertEquals(7, populatedRepo.size());
        assertFalse(populatedRepo.isEmpty());
        // not reading from the same files
        assertTrue(emptyRepo.isEmpty());

        populatedRepo.flush();
        assertDoesNotThrow(()-> populatedRepo.wipe());
        assertTrue(populatedRepo.isEmpty());
        populatedRepo.refreshFromFiles();
        assertTrue(populatedRepo.isEmpty());
    }

    @Test
    public void testStreak() {
        addConsecutiveSessionsInStreak(10);

        assertEquals(10, populatedRepo.getStreak(null));
        assertEquals(0, populatedRepo.getStreak("FakeSubject"));
    }

    @Test
    public void testGetNextID() {
        long firstID = populatedRepo.getNextID();
        populatedRepo.addSession(mockStudySession(firstID));

        long secondID = populatedRepo.getNextID();
        populatedRepo.addSession(mockStudySession(secondID));

        long thirdID = populatedRepo.getNextID();
        populatedRepo.addSession(mockStudySession(thirdID));

        assertNotEquals(firstID, secondID);
        assertNotEquals(secondID, thirdID);
        assertNotEquals(firstID, thirdID);

        assertEquals(3, populatedRepo.size());
        populatedRepo.deleteSession(firstID);

        assertEquals(2, populatedRepo.size());

        // -1 is not a possible ID
        populatedRepo.deleteSession(-1L);
        assertEquals(2, populatedRepo.size());

        populatedRepo.deleteSession(secondID);
        assertEquals(1, populatedRepo.size());

        populatedRepo.deleteSession(thirdID);
        assertEquals(0, populatedRepo.size());
    }

    @Test
    public void testPersistence() {
        StudySession session1 = mockStudySession(populatedRepo.getNextID());
        StudySession session2 = mockStudySession(populatedRepo.getNextID());

        populatedRepo.addSession(session1);
        populatedRepo.addSession(session2);

        populatedRepo.flush();

        StudyRepository newRepo = new StudyRepository(
                populatedRepo.getCompletePath(),
                populatedRepo.getActivePath());

        assertEquals(2, populatedRepo.size());
        assertEquals(2, newRepo.size());
    }



    public void addConsecutiveSessionsInStreak(int num) {
        LocalDateTime time = LocalDateTime.now();

        for (int i = 0; i < num; i++) {
            StudySession nextSession = mockStudySession(populatedRepo.getNextID());
            nextSession = nextSession.edit(time, null, null);
            populatedRepo.addSession(nextSession);
            time = time.minusDays(1L);
        }
    }

    public void addSessions(int numSession) {
        for (int i = 0; i < numSession; i++) {
            populatedRepo.addSession(mockStudySession(populatedRepo.getNextID()));
        }
    }

    public void addActiveSessions(int numSession) {
        for (int i = 0; i < numSession; i++) {
            populatedRepo.startSession(mockActiveSession());
        }
    }

    /**
     * Gets a date from 100 years ago to 11/25/2026
     * @return a localdatetime
     */
    public LocalDateTime getRandomDate() {
        return LocalDateTime.of(2026, 11, 25, 1, 56)
                .minusDays(random.nextLong(0, 365 * 100));
    }

    public StudySession mockStudySession(long id) {
        return mockStudySession(id, 1, "Unspecified");
    }

    public StudySession mockStudySession(long id, int durationHours, String subject) {
        LocalDateTime date = getRandomDate();

        return new StudySession(id, date, date.plusHours(durationHours), subject);
    }

    public ActiveSession mockActiveSession() {
        return mockActiveSession("Unspecified");
    }

    public ActiveSession mockActiveSession(String subject) {
        LocalDateTime date = getRandomDate();
        return new ActiveSession(date, subject);
    }

}


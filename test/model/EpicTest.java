package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Arrays;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

class EpicTest {

    private Epic testEpic;
    private Subtask testSubtask1;
    private Subtask testSubtask2;
    private final LocalDateTime firstSubtaskStartTime = LocalDateTime.of(2025, Month.JUNE, 1, 11, 0);

    @BeforeEach
    void setUp() {
        testEpic = new Epic("TestEpic", "TestDescription", 1);
        testSubtask1 = new Subtask("TestSubtask1", "TestDescription", 2,
                firstSubtaskStartTime, Duration.ofMinutes(30), 1);
        testSubtask2 = new Subtask("TestSubtask2", "TestDescription", 3,
                firstSubtaskStartTime.plusMinutes(35),
                Duration.ofMinutes(30), 1);
        testEpic.addSubtask(testSubtask1);
        testEpic.addSubtask(testSubtask2);
    }

    @Test
    void testGetSubtasks() {
        ArrayList<Subtask> expectedSubtasks = new ArrayList<>(Arrays.asList(testSubtask1, testSubtask2));
        ArrayList<Subtask> actualSubtasks = testEpic.getSubtasks();
        assertEquals(expectedSubtasks, actualSubtasks);
    }

    @Test
    void testAddSubtask() {
        assertEquals(2, testEpic.getSubtasks().size());
        Subtask testSubTask3 = new Subtask("TestSubtask3", "TestDescription", 4, 1);
        testEpic.addSubtask(testSubTask3);
        assertEquals(3, testEpic.getSubtasks().size());
    }

    @Test
    void testRemoveSubtask() {
        assertEquals(2, testEpic.getSubtasks().size());
        testEpic.removeSubtask(testSubtask1);
        assertEquals(1, testEpic.getSubtasks().size());
    }

    @Test
    void testEpicStatusShouldBeInProgressAfterSubtaskStatusChange() {
        assertEquals(TaskStatus.NEW, testEpic.getStatus());
        testSubtask1.setStatus(TaskStatus.IN_PROGRESS);
        testEpic.manageEpicStatus();
        assertEquals(TaskStatus.IN_PROGRESS, testEpic.getStatus());
    }

    @Test
    void testEpicStatusShouldBeDoneAfterAllSubtasksStatusAreDone() {
        assertEquals(TaskStatus.NEW, testEpic.getStatus());
        testSubtask1.setStatus(TaskStatus.DONE);
        testSubtask2.setStatus(TaskStatus.DONE);
        testEpic.manageEpicStatus();
        assertEquals(TaskStatus.DONE, testEpic.getStatus());
    }

    @Test
    void testStatusShouldNotChangeManually() {
        UnsupportedOperationException exception = assertThrows(UnsupportedOperationException.class, () -> {
            testEpic.setStatus(TaskStatus.DONE);
        });
        assertEquals("Статус у задачи типа EPIC контролируется автоматически.",
                exception.getMessage());
    }

    @Test
    void testToString() {
        Duration duration = Duration.ofHours(1);
        testEpic.setEndTime();
        String toStringAssertion = "model.Epic{taskName='TestEpic', description='TestDescription', " +
                "taskId=1, status='NEW', startTime=" + firstSubtaskStartTime.format(testEpic.formatter)
                + ", duration=" + duration.toHoursPart() + ":" + duration.toMinutesPart() + ", endTime="
                + firstSubtaskStartTime.plusHours(1).format(testEpic.formatter) + "}";
        assertEquals(toStringAssertion, testEpic.toString());
    }

    @Test
    void testSameEpicIdsShouldBeEqual() {
        Epic newEpic = new Epic("TestEpic2", "TestDescription2", 1);
        assertEquals(newEpic, testEpic);
    }

    @Test
    void testEpicGetStartTime() {
        assertEquals(firstSubtaskStartTime, testEpic.getStartTime());
    }

    @Test
    void testEpicGetDuration() {
        assertEquals(Duration.ofHours(1), testEpic.getDuration());
    }

    @Test
    void testEpicGetEndTime() {
        testEpic.setEndTime();
        assertEquals(firstSubtaskStartTime.plusHours(1), testEpic.getEndTime());
    }

    @Test
    void testEpicDurationAddedWithNewSubtask() {
        assertEquals(Duration.ofHours(1), testEpic.getDuration());
        Subtask subtask3 = new Subtask("TestSubtask2", "TestDescription", 3,
                firstSubtaskStartTime.plusMinutes(65),
                Duration.ofMinutes(45), 1);
        testEpic.addSubtask(subtask3);
        assertEquals(Duration.ofMinutes(105), testEpic.getDuration());

    }
}

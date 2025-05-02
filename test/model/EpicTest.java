package model;

import util.TaskStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.ArrayList;
import java.util.Arrays;

class EpicTest {
    private Epic testEpic;
    private Subtask testSubtask1;
    private Subtask testSubtask2;

    @BeforeEach
    void setUp() {
        testEpic = new Epic("TestEpic", "TestDescription", 1);
        testSubtask1 = new Subtask("TestSubtask1", "TestDescription", 2, 1);
        testSubtask2 = new Subtask("TestSubtask2", "TestDescription", 3, 1);
        testEpic.addSubtask(testSubtask1);
        testEpic.addSubtask(testSubtask2);
    }

    @Test
    void getSubtasks() {
        ArrayList<Subtask> expectedSubtasks = new ArrayList<>(Arrays.asList(testSubtask1, testSubtask2));
        ArrayList<Subtask> actualSubtasks = testEpic.getSubtasks();
        Assertions.assertEquals(expectedSubtasks, actualSubtasks);
    }

    @Test
    void addSubtask() {
        Assertions.assertEquals(2, testEpic.getSubtasks().size());
        Subtask testSubTask3 = new Subtask("TestSubtask3", "TestDescription", 4, 1);
        testEpic.addSubtask(testSubTask3);
        Assertions.assertEquals(3, testEpic.getSubtasks().size());
    }

    @Test
    void removeSubtask() {
        Assertions.assertEquals(2, testEpic.getSubtasks().size());
        testEpic.removeSubtask(testSubtask1);
        Assertions.assertEquals(1, testEpic.getSubtasks().size());
    }

    @Test
    void epicStatusShouldBeInProgressAfterSubtaskStatusChange() {
        Assertions.assertEquals(TaskStatus.NEW, testEpic.getStatus());
        testSubtask1.setStatus(TaskStatus.IN_PROGRESS);
        testEpic.manageEpicStatus();
        Assertions.assertEquals(TaskStatus.IN_PROGRESS, testEpic.getStatus());
    }

    @Test
    void epicStatusShouldBeDoneAfterAllSubtasksStatusAreDone() {
        Assertions.assertEquals(TaskStatus.NEW, testEpic.getStatus());
        testSubtask1.setStatus(TaskStatus.DONE);
        testSubtask2.setStatus(TaskStatus.DONE);
        testEpic.manageEpicStatus();
        Assertions.assertEquals(TaskStatus.DONE, testEpic.getStatus());
    }

    @Test
    void statusShouldNotChangeManually() {
        UnsupportedOperationException exception = Assertions.assertThrows(UnsupportedOperationException.class, () -> {
            testEpic.setStatus(TaskStatus.DONE);
        });
        Assertions.assertEquals("Статус у задачи типа EPIC контролируется автоматически.",
                exception.getMessage());
    }

    @Test
    void testToString() {
        String toStringAssertion = "model.Epic{taskName='TestEpic', description='TestDescription', " +
                "taskId=1, status='NEW'}";
        Assertions.assertEquals(toStringAssertion, testEpic.toString());
    }

    @Test
    void sameEpicIdsShouldBeEqual() {
        Epic newEpic = new Epic("TestEpic2", "TestDescription2", 1);
        Assertions.assertEquals(newEpic, testEpic);
    }
}
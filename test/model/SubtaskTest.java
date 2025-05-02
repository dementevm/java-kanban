package model;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;


class SubtaskTest {
    private Subtask testSubtask;

    @BeforeEach
    void setUp() {
        Epic testEpic = new Epic("Test Epic", "Epic Description", 1);
        testSubtask = new Subtask("Test Subtask", "Subtask Description", 2, testEpic.getTaskId());
    }

    @Test
    void getEpicId() {
        Assertions.assertEquals(1, testSubtask.getEpicId());
    }

    @Test
    void setEpicId() {
        testSubtask.setEpicId(3);
        Assertions.assertEquals(3, testSubtask.getEpicId());
    }

    @Test
    void testToString() {
        String toStringAssertion = "model.Subtask{taskName='Test Subtask', description='Subtask Description', " +
                "status='NEW', taskId=2, epicId=1}";
        Assertions.assertEquals(toStringAssertion, testSubtask.toString());
    }

    @Test
    void sameSubtaskShouldBeEqual() {
        Subtask newSubtask = new Subtask("Test Subtask2", "TestDescription2", 2, 1);
        Assertions.assertEquals(newSubtask, testSubtask);
    }
}
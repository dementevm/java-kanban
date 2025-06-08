package model;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;

class SubtaskTest {

    private Subtask testSubtask;

    @BeforeEach
    void setUp() {
        Epic testEpic = new Epic("Test Epic", "Epic Description", 1);
        testSubtask = new Subtask("Test Subtask", "Subtask Description", 2, testEpic.getTaskId());
    }

    @Test
    void testGetEpicId() {
        assertEquals(1, testSubtask.getEpicId());
    }

    @Test
    void testSetEpicId() {
        testSubtask.setEpicId(3);
        assertEquals(3, testSubtask.getEpicId());
    }

    @Test
    void testToString() {
        String toStringAssertion = "model.Subtask{taskName='Test Subtask', description='Subtask Description', " +
                "status='NEW', taskId=2, epicId=1}";
        assertEquals(toStringAssertion, testSubtask.toString());
    }

    @Test
    void testSameSubtaskShouldBeEqual() {
        Subtask newSubtask = new Subtask("Test Subtask2", "TestDescription2", 2, 1);
        assertEquals(newSubtask, testSubtask);
    }

    @Test
    void testToDataStorage() {
        String toStringAssertion = "2,SUBTASK,Test Subtask,NEW,Subtask Description,1";
        assertEquals(toStringAssertion, testSubtask.toDataStorageFile());
    }
}

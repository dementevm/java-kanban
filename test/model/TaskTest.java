package model;

import util.TaskStatus;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    private Task testTask;

    @BeforeEach
    void setUp() {
        testTask = new Task("Test Task", "Test Description", 1);
    }

    @Test
    void testGetTaskName() {
        assertEquals("Test Task", testTask.getTaskName());
    }

    @Test
    void testSetTaskName() {
        testTask.setTaskName("Changed Task");
        assertEquals("Changed Task", testTask.getTaskName());
    }

    @Test
    void testGetDescription() {
        assertEquals("Test Description", testTask.getDescription());
    }

    @Test
    void testSetDescription() {
        testTask.setDescription("Changed Description");
        assertEquals("Changed Description", testTask.getDescription());
    }

    @Test
    void testGetTaskId() {
        assertEquals(1, testTask.getTaskId());
    }

    @Test
    void testSetTaskId() {
        testTask.setTaskId(2);
        assertEquals(2, testTask.getTaskId());
    }

    @Test
    void testGetStatus() {
        assertEquals(TaskStatus.NEW, testTask.getStatus());
    }

    @Test
    void testSetStatus() {
        testTask.setStatus(TaskStatus.DONE);
        assertEquals(TaskStatus.DONE, testTask.getStatus());
    }

    @Test
    void testTasksWithSameIdShouldBeEquals() {
        Task newTask = new Task("Test Task 2", "Test Description 2", 1);
        assertEquals(testTask, newTask);
    }

    @Test
    void testHashCode() {
        Task hashedTestTask = new Task("Test Task", "Test Description", 1);
        assertEquals(testTask.hashCode(), hashedTestTask.hashCode());
    }

    @Test
    void testToString() {
        String toStringAssertion = "model.Task{taskName='Test Task', description='Test Description', " +
                "status='NEW', taskId=1}";
        assertEquals(toStringAssertion, testTask.toString());
    }
    
    @Test
    void testToDataStorage() {
        String toStringAssertion = "1,TASK,Test Task,NEW,Test Description,";
        assertEquals(toStringAssertion, testTask.toDataStorageFile());
    }
}
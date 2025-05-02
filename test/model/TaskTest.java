package model;

import util.TaskStatus;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class TaskTest {
    private Task testTask;

    @BeforeEach
    void setUp() {
        testTask = new Task("Test Task", "Test Description", 1);
    }

    @Test
    void getTaskName() {
        Assertions.assertEquals("Test Task", testTask.getTaskName());
    }

    @Test
    void setTaskName() {
        testTask.setTaskName("Changed Task");
        Assertions.assertEquals("Changed Task", testTask.getTaskName());
    }

    @Test
    void getDescription() {
        Assertions.assertEquals("Test Description", testTask.getDescription());
    }

    @Test
    void setDescription() {
        testTask.setDescription("Changed Description");
        Assertions.assertEquals("Changed Description", testTask.getDescription());
    }

    @Test
    void getTaskId() {
        Assertions.assertEquals(1, testTask.getTaskId());
    }

    @Test
    void setTaskId() {
        testTask.setTaskId(2);
        Assertions.assertEquals(2, testTask.getTaskId());
    }

    @Test
    void getStatus() {
        Assertions.assertEquals(TaskStatus.NEW, testTask.getStatus());
    }

    @Test
    void setStatus() {
        testTask.setStatus(TaskStatus.DONE);
        Assertions.assertEquals(TaskStatus.DONE, testTask.getStatus());
    }

    @Test
    void tasksWithSameIdShouldBeEquals() {
        Task newTask = new Task("Test Task 2", "Test Description 2", 1);
        Assertions.assertEquals(testTask, newTask);
    }

    @Test
    void testHashCode() {
        Task hashedTestTask = new Task("Test Task", "Test Description", 1);
        Assertions.assertEquals(testTask.hashCode(), hashedTestTask.hashCode());
    }

    @Test
    void testToString() {
        String toStringAssertion = "model.Task{taskName='Test Task', description='Test Description', " +
                "status='NEW', taskId=1}";
        Assertions.assertEquals(toStringAssertion, testTask.toString());
    }
}
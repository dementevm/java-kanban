package controller;

import exceptions.TaskNotFound;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.LinkedList;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    private InMemoryTaskManager taskManager;

    @BeforeEach
    void setUp() throws IOException, TaskNotFound {
        taskManager = new InMemoryTaskManager();
        for (int i = 0; i < 10; i++) {
            Task task = new Task("Task" + (i + 1), "Description" + (i + 1));
            taskManager.createTask(task);
        }
        taskManager.getTask(1);
    }

    @Test
    void testGetHistory() {
        LinkedList<Task> expectedHistoryList = new LinkedList<>();
        expectedHistoryList.add(taskManager.getTasks().get(0));
        assertEquals(expectedHistoryList, taskManager.getHistory());
    }

    @Test
    void testAdd() throws TaskNotFound {
        assertEquals(1, taskManager.getHistory().size());
        for (int i = 2; i <= 10; i++) {
            taskManager.getTask(i);
        }
        assertEquals(10, taskManager.getHistory().size());
        assertEquals("Task1", taskManager.getHistory().getFirst().getTaskName());
        assertEquals("Task10", taskManager.getHistory().getLast().getTaskName());
        taskManager.getTask(5);
        assertEquals("Task2", taskManager.getHistory().getFirst().getTaskName());
        assertEquals("Task5", taskManager.getHistory().getLast().getTaskName());
    }

    @Test
    void testPreviousTaskVersionShouldBeInHistory() throws IOException, TaskNotFound {
        LinkedList<Task> expectedHistoryList = new LinkedList<>();
        expectedHistoryList.add(taskManager.getTasks().getFirst());
        Task updatedTask = new Task("Task1Updated", "Description1Updated", 1);
        taskManager.updateTask(updatedTask);
        expectedHistoryList.add(taskManager.getTasks().getFirst());
        taskManager.getTask(1);
        assertEquals(expectedHistoryList, taskManager.getHistory());
    }
}
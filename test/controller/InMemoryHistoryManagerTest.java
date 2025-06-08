package controller;

import exceptions.TaskNotFound;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.assertEquals;

class InMemoryHistoryManagerTest {

    private InMemoryTaskManager taskManager;
    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void setUp() throws IOException, TaskNotFound {
        taskManager = new InMemoryTaskManager();
        historyManager = new InMemoryHistoryManager();
        for (int i = 0; i < 10; i++) {
            Task task = new Task("Task" + (i + 1), "Description" + (i + 1));
            taskManager.createTask(task);
        }
        taskManager.getTask(1);
    }

    @Test
    void testGetHistory() throws TaskNotFound {
        assertEquals(1, taskManager.getHistory().size());
        taskManager.getTask(2);
        taskManager.getTask(3);
        taskManager.getTask(4);
        taskManager.getTask(5);
        assertEquals(5, taskManager.getHistory().size());
        taskManager.getTask(2);
        assertEquals(5, taskManager.getHistory().size());
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
        assertEquals("Task5", taskManager.getHistory().getLast().getTaskName());
        assertEquals(10, taskManager.getHistory().size());
    }
}

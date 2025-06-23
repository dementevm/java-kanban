package controller;

import exceptions.TaskNotFound;
import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.util.List;

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

    @Test
    void testRemoveFromHistory_BeginningMiddleEnd() throws IOException, TaskNotFound {
        Task task1 = new Task("Task1", "Desc1", 1);
        Task task2 = new Task("Task2", "Desc2", 2);
        Task task3 = new Task("Task3", "Desc3", 3);
        Task task4 = new Task("Task4", "Desc4", 4);
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task3);
        historyManager.add(task4);

        // Act & Assert: remove from beginning
        historyManager.remove(1);
        assertEquals(List.of(task2, task3, task4), historyManager.getHistory());

        // Act & Assert: remove from end
        historyManager.remove(4);
        assertEquals(List.of(task2, task3), historyManager.getHistory());

        // Act & Assert: remove from middle
        historyManager.remove(3);
        assertEquals(List.of(task2), historyManager.getHistory());
    }
}

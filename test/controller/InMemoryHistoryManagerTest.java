package controller;

import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;


class InMemoryHistoryManagerTest {
    private InMemoryTaskManager taskManager;
    private InMemoryHistoryManager historyManager;

    @BeforeEach
    void setUp() {
        taskManager = new InMemoryTaskManager();
        for (int i = 0; i < 10; i++) {
            Task task = new Task("Task" + (i + 1), "Description" + (i + 1));
            taskManager.createTask(task);
        }
        taskManager.getTask(1);
    }

    @Test
    void getHistory() {
        Assertions.assertEquals(1, taskManager.getHistory().size());
        taskManager.getTask(2);
        taskManager.getTask(3);
        taskManager.getTask(4);
        taskManager.getTask(5);
        Assertions.assertEquals(5, taskManager.getHistory().size());
        taskManager.getTask(2);
        Assertions.assertEquals(5, taskManager.getHistory().size());
    }

    @Test
    void add() {
        Assertions.assertEquals(1, taskManager.getHistory().size());
        for (int i = 2; i <= 10; i++) {
            taskManager.getTask(i);
        }
        Assertions.assertEquals(10, taskManager.getHistory().size());
        Assertions.assertEquals("Task1", taskManager.getHistory().getFirst().getTaskName());
        Assertions.assertEquals("Task10", taskManager.getHistory().getLast().getTaskName());
        taskManager.getTask(5);
        Assertions.assertEquals("Task5", taskManager.getHistory().getLast().getTaskName());
        Assertions.assertEquals(10, taskManager.getHistory().size());
    }

    @Test
    void linkLast() {
        for (int i = 2; i <=5; i++) {
            Task task = taskManager.getTask(i);
            Assertions.assertEquals(task, taskManager.getHistory().getLast());
        }
    }
}
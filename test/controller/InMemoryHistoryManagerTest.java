package controller;

import model.Task;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.Assertions;

import java.util.LinkedList;

class InMemoryHistoryManagerTest {
    private InMemoryTaskManager taskManager;

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
        LinkedList<Task> expectedHistoryList = new LinkedList<>();
        expectedHistoryList.add(taskManager.getTasks().get(0));
        Assertions.assertEquals(expectedHistoryList, taskManager.getHistory());
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
        Assertions.assertEquals("Task2", taskManager.getHistory().getFirst().getTaskName());
        Assertions.assertEquals("Task5", taskManager.getHistory().getLast().getTaskName());
    }

    @Test
    void previousTaskVersionShouldBeInHistory() {
        LinkedList<Task> expectedHistoryList = new LinkedList<>();
        expectedHistoryList.add(taskManager.getTasks().getFirst());
        Task updatedTask = new Task("Task1Updated", "Description1Updated", 1);
        taskManager.updateTask(updatedTask);
        expectedHistoryList.add(taskManager.getTasks().getFirst());
        taskManager.getTask(1);
        Assertions.assertEquals(expectedHistoryList, taskManager.getHistory());
    }
}
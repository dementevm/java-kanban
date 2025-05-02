package controller;

import model.Epic;
import model.Task;
import model.Subtask;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Managers;

import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;
    private Task testTask;
    private Epic testEpic;
    private Subtask testSubtask;


    @BeforeEach
    void setUp() {
        taskManager = Managers.getDefault();
        testTask = new Task("TestTask", "TestDescription");
        testEpic = new Epic("TestEpic2", "TestDescription2");
        taskManager.createTask(testTask);
        taskManager.createEpic(testEpic);
        testSubtask = new Subtask("TestSubtask", "TestDescription", testEpic.getTaskId());
        taskManager.createSubtask(testSubtask);
    }

    @Test
    void getTaskStorage() {
        HashMap<Integer, Task> expectedTaskStorage = new HashMap<>();
        expectedTaskStorage.put(1, testTask);
        expectedTaskStorage.put(2, testEpic);
        expectedTaskStorage.put(3, testSubtask);
        Assertions.assertEquals(expectedTaskStorage, taskManager.getTaskStorage());
    }

    @Test
    void getHistory() {
        taskManager.getTask(1);
        taskManager.getEpic(2);
        taskManager.getSubtask(3);
        List<Task> expectedHistory = new LinkedList<>();
        expectedHistory.add(testTask);
        expectedHistory.add(testEpic);
        expectedHistory.add(testSubtask);
        Assertions.assertEquals(expectedHistory, taskManager.getHistory());
        Task updatedTask = new Task("UpdatedTask", "UpdatedDescription", testTask.getTaskId());
        taskManager.updateTask(updatedTask);
        taskManager.getTask(1);
        expectedHistory.add(updatedTask);
        Assertions.assertEquals(expectedHistory, taskManager.getHistory());
    }

    @Test
    void createTask() {
        Task newTask = new Task("TestTask", "TestDescription");
        taskManager.createTask(newTask);
        Assertions.assertEquals(4, taskManager.getTaskStorage().size());
    }

    @Test
    void getTasks() {
        HashMap<Integer, Task> expectedTasks = new HashMap<>();
        expectedTasks.put(testTask.getTaskId(), testTask);
        Assertions.assertArrayEquals(expectedTasks.values().toArray(), taskManager.getTasks().toArray());
    }

    @Test
    void getTask() {
        Task expectedTask = new Task("TestTask", "TestDescription", 1);
        Task actualTask = taskManager.getTask(1);
        Assertions.assertEquals(expectedTask, actualTask);
    }

    @Test
    void deleteTasks() {
        Assertions.assertEquals(1, taskManager.getTasks().size());
        taskManager.deleteTasks();
        Assertions.assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void deleteTask() {
        Assertions.assertEquals(1, taskManager.getTasks().size());
        taskManager.deleteTask(1);
        Assertions.assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void updateTask() {
        Task updatedTask = new Task("TestTaskUpdated", "TestDescriptionUpdated", 1);
        taskManager.updateTask(updatedTask);
        Task expectedTask = taskManager.getTask(1);
        Assertions.assertEquals(expectedTask.getTaskName(), updatedTask.getTaskName());
    }

    @Test
    void createSubtask() {
        Subtask newSubtask = new Subtask("TestSubtask", "TestDescription", testEpic.getTaskId());
        taskManager.createSubtask(newSubtask);
        Assertions.assertEquals(4, taskManager.getTaskStorage().size());
    }

    @Test
    void getSubtasks() {
        HashMap<Integer, Subtask> expectedSubtasks = new HashMap<>();
        expectedSubtasks.put(testSubtask.getTaskId(), testSubtask);
        Assertions.assertArrayEquals(expectedSubtasks.values().toArray(), taskManager.getSubtasks().toArray());
    }

    @Test
    void getSubtask() {
        Subtask expectedSubtask = new Subtask("TestSubtask", "TestDescription", 3, 2);
        Subtask actualSubtask = taskManager.getSubtask(3);
        Assertions.assertEquals(expectedSubtask, actualSubtask);
    }

    @Test
    void deleteSubtasks() {
        Assertions.assertEquals(1, taskManager.getSubtasks().size());
        taskManager.deleteSubtasks();
        Assertions.assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    void deleteSubtask() {
        Assertions.assertEquals(1, taskManager.getSubtasks().size());
        taskManager.deleteSubtask(3);
        Assertions.assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    void updateSubtask() {
        Subtask updatedSubtask = new Subtask("TestSubtaskUpdated", "TestDescriptionUpdated", 3, 2);
        taskManager.updateSubtask(updatedSubtask);
        Subtask expectedSubtask = taskManager.getSubtask(3);
        Assertions.assertEquals(expectedSubtask.getTaskName(), updatedSubtask.getTaskName());
    }

    @Test
    void createEpic() {
        Epic newEpic = new Epic("TestEpic", "TestDescription");
        taskManager.createEpic(newEpic);
        Assertions.assertEquals(4, taskManager.getTaskStorage().size());
    }

    @Test
    void getEpics() {
        HashMap<Integer, Epic> expectedEpics = new HashMap<>();
        expectedEpics.put(testEpic.getTaskId(), testEpic);
        Assertions.assertArrayEquals(expectedEpics.values().toArray(), taskManager.getEpics().toArray());
    }

    @Test
    void getEpic() {
        Epic expectedEpic = new Epic("TestEpic", "TestDescription", 2);
        Epic actualEpic = taskManager.getEpic(2);
        Assertions.assertEquals(expectedEpic, actualEpic);
    }

    @Test
    void deleteEpics() {
        Assertions.assertEquals(1, taskManager.getEpics().size());
        taskManager.deleteEpics();
        Assertions.assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    void deleteEpic() {
        Assertions.assertEquals(1, taskManager.getEpics().size());
        taskManager.deleteEpic(2);
        Assertions.assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    void updateEpic() {
        Epic updatedEpic = new Epic("TestEpicUpdated", "TestDescriptionUpdated", 2);
        taskManager.updateEpic(updatedEpic);
        Epic expectedEpic = taskManager.getEpic(2);
        Assertions.assertEquals(expectedEpic.getTaskName(), updatedEpic.getTaskName());
    }
}
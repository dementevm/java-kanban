package controller;

import exceptions.TaskNotFound;
import model.Epic;
import model.Task;
import model.Subtask;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import util.Managers;

import java.io.IOException;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryTaskManagerTest {
    private TaskManager taskManager;
    private Task testTask;
    private Epic testEpic;
    private Subtask testSubtask;


    @BeforeEach
    void setUp() throws IOException, TaskNotFound {
        taskManager = Managers.getDefault();
        testTask = new Task("TestTask", "TestDescription");
        testEpic = new Epic("TestEpic2", "TestDescription2");
        taskManager.createTask(testTask);
        taskManager.createEpic(testEpic);
        testSubtask = new Subtask("TestSubtask", "TestDescription", testEpic.getTaskId());
        taskManager.createSubtask(testSubtask);
    }

    @Test
    void testGetTaskStorage() {
        HashMap<Integer, Task> expectedTaskStorage = new HashMap<>();
        expectedTaskStorage.put(1, testTask);
        expectedTaskStorage.put(2, testEpic);
        expectedTaskStorage.put(3, testSubtask);
        assertEquals(expectedTaskStorage, taskManager.getTaskStorage());
    }

    @Test
    void testGetHistory() throws IOException, TaskNotFound {
        taskManager.getTask(1);
        taskManager.getEpic(2);
        taskManager.getSubtask(3);
        List<Task> expectedHistory = new LinkedList<>();
        expectedHistory.add(testTask);
        expectedHistory.add(testEpic);
        expectedHistory.add(testSubtask);
        assertEquals(expectedHistory, taskManager.getHistory());
        Task updatedTask = new Task("UpdatedTask", "UpdatedDescription", testTask.getTaskId());
        taskManager.updateTask(updatedTask);
        taskManager.getTask(1);
        expectedHistory.removeFirst();
        expectedHistory.add(updatedTask);
        assertEquals(expectedHistory, taskManager.getHistory());
    }

    @Test
    void testCreateTask() throws IOException {
        Task newTask = new Task("TestTask", "TestDescription");
        taskManager.createTask(newTask);
        assertEquals(4, taskManager.getTaskStorage().size());
    }

    @Test
    void testGetTasks() {
        HashMap<Integer, Task> expectedTasks = new HashMap<>();
        expectedTasks.put(testTask.getTaskId(), testTask);
        assertArrayEquals(expectedTasks.values().toArray(), taskManager.getTasks().toArray());
    }

    @Test
    void testGetTask() throws TaskNotFound {
        Task expectedTask = new Task("TestTask", "TestDescription", 1);
        Task actualTask = taskManager.getTask(1);
        assertEquals(expectedTask, actualTask);
    }

    @Test
    void testDeleteTasks() throws IOException {
        assertEquals(1, taskManager.getTasks().size());
        taskManager.deleteTasks();
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void testDeleteTask() throws IOException, TaskNotFound {
        assertEquals(1, taskManager.getTasks().size());
        taskManager.deleteTask(1);
        assertEquals(0, taskManager.getTasks().size());
    }

    @Test
    void testUpdateTask() throws TaskNotFound, IOException {
        Task updatedTask = new Task("TestTaskUpdated", "TestDescriptionUpdated", 1);
        taskManager.updateTask(updatedTask);
        Task expectedTask = taskManager.getTask(1);
        assertEquals(expectedTask.getTaskName(), updatedTask.getTaskName());
    }

    @Test
    void testUpdateTaskWithSameData() throws TaskNotFound, IOException {
        Task originalTask = taskManager.getTask(1);
        Task identicalTask = new Task(originalTask.getTaskName(), originalTask.getDescription(), originalTask.getTaskId());
        taskManager.updateTask(identicalTask);
        Task resultTask = taskManager.getTask(1);
        assertEquals(originalTask.getTaskName(), resultTask.getTaskName());
        assertEquals(originalTask.getDescription(), resultTask.getDescription());
        assertEquals(originalTask.getTaskId(), resultTask.getTaskId());
    }

    @Test
    void testCreateSubtask() throws IOException, TaskNotFound {
        Subtask newSubtask = new Subtask("TestSubtask", "TestDescription", testEpic.getTaskId());
        taskManager.createSubtask(newSubtask);
        assertEquals(4, taskManager.getTaskStorage().size());
    }

    @Test
    void testGetSubtasks() {
        HashMap<Integer, Subtask> expectedSubtasks = new HashMap<>();
        expectedSubtasks.put(testSubtask.getTaskId(), testSubtask);
        assertArrayEquals(expectedSubtasks.values().toArray(), taskManager.getSubtasks().toArray());
    }

    @Test
    void testGetSubtask() throws TaskNotFound {
        Subtask expectedSubtask = new Subtask("TestSubtask", "TestDescription", 3, 2);
        Subtask actualSubtask = taskManager.getSubtask(3);
        assertEquals(expectedSubtask, actualSubtask);
    }

    @Test
    void testDeleteSubtasks() throws IOException {
        assertEquals(1, taskManager.getSubtasks().size());
        taskManager.deleteSubtasks();
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    void testDeleteSubtask() throws IOException, TaskNotFound {
        assertEquals(1, taskManager.getSubtasks().size());
        taskManager.deleteSubtask(3);
        assertEquals(0, taskManager.getSubtasks().size());
    }

    @Test
    void testUpdateSubtask() throws IOException, TaskNotFound {
        Subtask updatedSubtask = new Subtask("TestSubtaskUpdated", "TestDescriptionUpdated", 3, 2);
        taskManager.updateSubtask(updatedSubtask);
        Subtask expectedSubtask = taskManager.getSubtask(3);
        assertEquals(expectedSubtask.getTaskName(), updatedSubtask.getTaskName());
    }

    @Test
    void testCreateEpic() throws IOException {
        Epic newEpic = new Epic("TestEpic", "TestDescription");
        taskManager.createEpic(newEpic);
        assertEquals(4, taskManager.getTaskStorage().size());
    }

    @Test
    void testGetEpics() {
        HashMap<Integer, Epic> expectedEpics = new HashMap<>();
        expectedEpics.put(testEpic.getTaskId(), testEpic);
        assertArrayEquals(expectedEpics.values().toArray(), taskManager.getEpics().toArray());
    }

    @Test
    void testGetEpic() throws TaskNotFound {
        Epic expectedEpic = new Epic("TestEpic", "TestDescription", 2);
        Epic actualEpic = taskManager.getEpic(2);
        assertEquals(expectedEpic, actualEpic);
    }

    @Test
    void testDeleteEpics() throws IOException {
        assertEquals(1, taskManager.getEpics().size());
        taskManager.deleteEpics();
        assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    void testDeleteEpic() throws IOException, TaskNotFound {
        assertEquals(1, taskManager.getEpics().size());
        taskManager.deleteEpic(2);
        assertEquals(0, taskManager.getEpics().size());
    }

    @Test
    void testUpdateEpic() throws IOException, TaskNotFound {
        Epic updatedEpic = new Epic("TestEpicUpdated", "TestDescriptionUpdated", 2);
        taskManager.updateEpic(updatedEpic);
        Epic expectedEpic = taskManager.getEpic(2);
        assertEquals(expectedEpic.getTaskName(), updatedEpic.getTaskName());
    }

    @Test
    void testCreateSubtask_NonExistentEpic_ThrowsException() {
        Subtask subtask = new Subtask("Subtask1", "SubDescription", 999);
        assertThrows(TaskNotFound.class, () -> taskManager.createSubtask(subtask));
    }

    @Test
    void testUpdateTask_TaskDoesntExist() {
        Task updatedTask = new Task("TestTaskUpdated", "TestDescriptionUpdated", 999);
        assertThrows(TaskNotFound.class, () -> taskManager.updateTask(updatedTask));
    }

    @Test
    void testUpdateSubtask_TaskDoesntExist() {
        Subtask subtask = new Subtask("Subtask1", "SubDescription", 999);
        assertThrows(TaskNotFound.class, () -> taskManager.updateSubtask(subtask));
    }

    @Test
    void testEpicUpdateTask_TaskDoesntExist() {
        Epic updatedEpic = new Epic("TestEpic", "TestDescription", 999);
        assertThrows(TaskNotFound.class, () -> taskManager.updateEpic(updatedEpic));
    }

    @Test
    void testDeleteTask_TaskDoesntExist() {
        assertThrows(TaskNotFound.class, () -> taskManager.deleteTask(999));
    }

    @Test
    void testDeleteSubtask_TaskDoesntExist() {
        assertThrows(TaskNotFound.class, () -> taskManager.deleteSubtask(999));
    }

    @Test
    void testDeleteEpic_TaskDoesntExist() {
        assertThrows(TaskNotFound.class, () -> taskManager.deleteEpic(999));
    }
}
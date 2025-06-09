package controller;

import exceptions.TaskNotFound;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.File;
import java.nio.file.Files;
import java.io.BufferedWriter;
import java.io.FileWriter;
import java.nio.file.Paths;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertNotNull;

class FileBackedTaskManagerTest {
    private FileBackedTaskManager taskManager;

    @BeforeEach
    void setUp() throws IOException, TaskNotFound {
        taskManager = new FileBackedTaskManager(Paths.get("src", "resources", "testDataStorage.csv"));
    }

    @AfterEach
    void tearDown() throws IOException {
        Files.deleteIfExists(taskManager.getFileStoragePath());
    }

    @Test
    void testCreateTask_WritesToFile() throws IOException {
        Task task = new Task("Task1", "Description1");
        taskManager.createTask(task);
        List<String> lines = Files.readAllLines(taskManager.getFileStoragePath());
        assertTrue(lines.stream().anyMatch(line -> line.contains("Task1")));
    }

    @Test
    void testCreateEpic_WritesToFile() throws IOException {
        Epic epic = new Epic("Epic1", "EpicDescription");
        taskManager.createEpic(epic);
        List<String> lines = Files.readAllLines(taskManager.getFileStoragePath());
        assertTrue(lines.stream().anyMatch(line -> line.contains("Epic1")));
    }

    @Test
    void testCreateSubtask_WritesToFileAndUpdatesEpic() throws IOException, TaskNotFound {
        Epic epic = new Epic("Epic1", "EpicDescription");
        int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask1", "SubtaskDescription", epicId);
        taskManager.createSubtask(subtask);
        List<String> lines = Files.readAllLines(taskManager.getFileStoragePath());
        assertTrue(lines.stream().anyMatch(line -> line.contains("Epic1")));
        assertEquals("2,SUBTASK,Subtask1,NEW,SubtaskDescription,1", lines.getLast());
    }

    @Test
    void testDeleteTask_RemovesFromFile() throws IOException, TaskNotFound {
        Task task = new Task("Task1", "Description1");
        int id = taskManager.createTask(task);
        taskManager.deleteTask(id);
        List<String> lines = Files.readAllLines(taskManager.getFileStoragePath());
        assertFalse(lines.stream().anyMatch(line -> line.contains("Task1")));
    }

    @Test
    void testDeleteEpic_RemovesFromFile() throws IOException, TaskNotFound {
        Epic epic = new Epic("Epic1", "EpicDescription");
        int id = taskManager.createEpic(epic);
        taskManager.deleteEpic(id);
        List<String> lines = Files.readAllLines(taskManager.getFileStoragePath());
        assertFalse(lines.stream().anyMatch(line -> line.contains("Epic1")));
    }

    @Test
    void testDeleteSubtask_RemovesFromFile() throws IOException, TaskNotFound {
        Epic epic = new Epic("Epic1", "EpicDescription");
        int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask1", "SubDescription", epicId);
        int subId = taskManager.createSubtask(subtask);
        taskManager.deleteSubtask(subId);
        List<String> lines = Files.readAllLines(taskManager.getFileStoragePath());
        assertFalse(lines.stream().anyMatch(line -> line.contains("Subtask1")));
    }

    @Test
    void testUpdateTask_UpdatesFile() throws IOException, TaskNotFound {
        Task task = new Task("Task1", "Description1");
        int id = taskManager.createTask(task);
        Task updated = new Task("Task1Updated", "Description1Updated");
        updated.setTaskId(id);
        taskManager.updateTask(updated);
        List<String> lines = Files.readAllLines(taskManager.getFileStoragePath());
        assertTrue(lines.stream().anyMatch(line -> line.contains("Task1Updated")));
        assertFalse(lines.stream().anyMatch(line -> line.contains("Task1,Description1")));
    }

    @Test
    void testUpdateEpic_UpdatesFile() throws IOException, TaskNotFound {
        Epic epic = new Epic("Epic1", "EpicDescription");
        int id = taskManager.createEpic(epic);
        Epic updated = new Epic("Epic1Updated", "EpicDescriptionUpdated");
        updated.setTaskId(id);
        taskManager.updateEpic(updated);
        List<String> lines = Files.readAllLines(taskManager.getFileStoragePath());
        assertTrue(lines.stream().anyMatch(line -> line.contains("Epic1Updated")));
        assertFalse(lines.stream().anyMatch(line -> line.contains("Epic1,EpicDescription")));
    }

    @Test
    void testUpdateSubtask_UpdatesFile() throws IOException, TaskNotFound {
        Epic epic = new Epic("Epic1", "EpicDescription");
        int epicId = taskManager.createEpic(epic);
        Subtask subtask = new Subtask("Subtask1", "SubDescription", epicId);
        int subId = taskManager.createSubtask(subtask);
        Subtask updated = new Subtask("Subtask1Updated", "SubDescriptionUpdated", epicId);
        updated.setTaskId(subId);
        taskManager.updateSubtask(updated);
        List<String> lines = Files.readAllLines(taskManager.getFileStoragePath());
        assertTrue(lines.stream().anyMatch(line -> line.contains("Subtask1Updated")));
        assertFalse(lines.stream().anyMatch(line -> line.contains("Subtask1,SubDescription")));
    }

    @Test
    void testDeleteTasks_RemovesAllTasksFromFile() throws IOException {
        taskManager.createTask(new Task("Task1", "Description1"));
        taskManager.createTask(new Task("Task2", "Description2"));
        taskManager.deleteTasks();
        List<String> lines = Files.readAllLines(taskManager.getFileStoragePath());
        assertFalse(lines.stream().anyMatch(line -> line.contains("Task1")));
        assertFalse(lines.stream().anyMatch(line -> line.contains("Task2")));
    }

    @Test
    void testDeleteEpics_RemovesAllEpicsFromFile() throws IOException {
        taskManager.createEpic(new Epic("Epic1", "EpicDescription"));
        taskManager.createEpic(new Epic("Epic2", "EpicDescription2"));
        taskManager.deleteEpics();
        List<String> lines = Files.readAllLines(taskManager.getFileStoragePath());
        assertFalse(lines.stream().anyMatch(line -> line.contains("Epic1")));
        assertFalse(lines.stream().anyMatch(line -> line.contains("Epic2")));
    }

    @Test
    void testDeleteSubtasks_RemovesAllSubtasksFromFile() throws IOException, TaskNotFound {
        Epic epic = new Epic("Epic1", "EpicDescription");
        int epicId = taskManager.createEpic(epic);
        taskManager.createSubtask(new Subtask("Subtask1", "SubDescription", epicId));
        taskManager.createSubtask(new Subtask("Sub2", "SubDescription2", epicId));
        taskManager.deleteSubtasks();
        List<String> lines = Files.readAllLines(taskManager.getFileStoragePath());
        assertFalse(lines.stream().anyMatch(line -> line.contains("Subtask1")));
        assertFalse(lines.stream().anyMatch(line -> line.contains("Sub2")));
    }

    @Test
    void testFileCreatedWithHeaderIfNotExists() throws IOException {
        List<String> lines = Files.readAllLines(taskManager.getFileStoragePath());
        assertFalse(lines.isEmpty());
        assertEquals("id,type,name,status,description,epic", lines.getFirst());
    }

    @Test
    void testLoadFromFile_NullFile_ThrowsException() {
        assertThrows(
                exceptions.ManagerSaveException.class,
                () -> FileBackedTaskManager.loadFromFile(null)
        );
    }

    @Test
    void testLoadFromFile_fileDoesntExist_ThrowsException() {
        File file = new File("src/resources/does_not_exist.csv");
        assertThrows(
                exceptions.ManagerSaveException.class,
                () -> FileBackedTaskManager.loadFromFile(file)
        );
    }

    @Test
    void testLoadFromFile_EmptyFile_ReturnsManagerWithNoTasks() throws Exception {
        File testFile = File.createTempFile("test", "csv");
        testFile.deleteOnExit();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();
        }
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(testFile);
        assertNotNull(manager);
        assertTrue(manager.getTaskStorage().isEmpty());
        assertTrue(manager.getTasks().isEmpty());
        assertTrue(manager.getEpics().isEmpty());
        assertTrue(manager.getSubtasks().isEmpty());
    }

    @Test
    void testLoadFromFile_WithTasksAndEpicsAndSubtasks() throws Exception {
        File testFile = File.createTempFile("test", "csv");
        testFile.deleteOnExit();
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(testFile))) {
            writer.write("id,type,name,status,description,epic");
            writer.newLine();
            writer.write("1,TASK,Task1,NEW,Description1,");
            writer.newLine();
            writer.write("2,EPIC,Epic1,NEW,Description2,");
            writer.newLine();
            writer.write("3,SUBTASK,Subtask1,NEW,Description3,2");
            writer.newLine();
        }
        FileBackedTaskManager manager = FileBackedTaskManager.loadFromFile(testFile);
        List<Task> tasks = manager.getTasks();
        assertEquals(1, tasks.size());
        assertEquals("Task1", tasks.getFirst().getTaskName());
        List<Epic> epics = manager.getEpics();
        assertEquals(1, epics.size());
        assertEquals("Epic1", epics.getFirst().getTaskName());
        List<Subtask> subtasks = manager.getSubtasks();
        assertEquals(1, subtasks.size());
        assertEquals("Subtask1", subtasks.getFirst().getTaskName());
        assertEquals(2, subtasks.getFirst().getEpicId());
        Epic epic = epics.getFirst();
        assertEquals(1, epic.getSubtasks().size());
        assertEquals("Subtask1", epic.getSubtasks().getFirst().getTaskName());
    }
}

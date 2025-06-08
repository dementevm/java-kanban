package controller;

import exceptions.TaskNotFound;
import model.Epic;
import model.Subtask;
import model.Task;
import util.CsvUtils;
import util.TaskStatus;

import java.io.*;
import java.nio.file.*;
import java.util.ArrayList;
import java.util.Comparator;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Path fileStoragePath;

    public FileBackedTaskManager() throws IOException, TaskNotFound {
        super();
        this.fileStoragePath = Paths.get("src" ,"resources", "dataStorage.csv");
        initiateFileStorage();
    }

    public FileBackedTaskManager(Path fileStoragePath) throws TaskNotFound, IOException {
        super();
        this.fileStoragePath = fileStoragePath;
        initiateFileStorage();
    }

    private void initiateFileStorage() throws IOException, TaskNotFound {
        if (!Files.exists(fileStoragePath)) {
            Files.createFile(fileStoragePath);
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileStoragePath.toFile()))) {
                writer.write("id,type,name,status,description,epic");
                writer.newLine();
            }
        } else {
            ArrayList<String> dataFromFile = CsvUtils.getDataFromFile(fileStoragePath);
            dataFromFile.sort(Comparator.comparingInt(s -> {
                String type = s.split(",")[1];
                return switch (type) {
                    case "TASK" -> 0;
                    case "EPIC" -> 1;
                    case "SUBTASK" -> 2;
                    default -> 3;
                    // Заглушка чтобы был default
                };
            }));
            if (!dataFromFile.isEmpty()) {
                for (String line : dataFromFile) {
                    String[] fields = line.split(",");
                    int id = Integer.parseInt(fields[0]);
                    String type = fields[1];
                    String name = fields[2];
                    TaskStatus status = TaskStatus.valueOf(fields[3]);
                    String description = fields[4];

                    switch (type) {
                        case "TASK":
                            Task task = new Task(name, description, id, status);
                            tasks.put(id, task);
                            taskStorage.put(id, task);
                            break;
                        case "EPIC":
                            Epic epic = new Epic(name, description, id, status);
                            epics.put(id, epic);
                            taskStorage.put(id, epic);
                            break;
                        case "SUBTASK":
                            Subtask subtask;
                            if (fields.length == 6) {
                                int epicId = Integer.parseInt(fields[5]);
                                subtask = new Subtask(name, description, id, epicId, status);
                                getEpic(epicId).addSubtask(subtask);
                            } else {
                                subtask = new Subtask(name, description, id, status);
                            }
                            subtasks.put(id, subtask);
                            taskStorage.put(id, subtask);
                            break;
                    }
                }
            }
        }
    }

    public Path getFileStoragePath() {
        return fileStoragePath;
    }

    private void save(Task task) throws IOException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileStoragePath.toFile(), true))) {
            writer.write(task.toDataStorageFile());
            writer.newLine();
        }
    }

    private void delete(int column, String value) throws IOException {
        CsvUtils.deleteLine(fileStoragePath, column, value);
    }

    private void update(String value, String newValue) throws IOException {
        CsvUtils.updateLine(fileStoragePath, value, newValue);
    }

    @Override
    public int createTask(Task task) throws IOException {
        super.createTask(task);
        save(task);
        return task.getTaskId();
    }

    @Override
    public void deleteTasks() throws IOException {
        super.deleteTasks();
        delete(1, "TASK");
    }

    @Override
    public void deleteTask(int id) throws IOException, TaskNotFound {
        super.deleteTask(id);
        delete(0, String.valueOf(id));
    }

    @Override
    public Integer updateTask(Task updatedTask) throws IOException, TaskNotFound {
        super.updateTask(updatedTask);
        int taskId = updatedTask.getTaskId();
        update(String.valueOf(taskId), updatedTask.toDataStorageFile());
        return taskId;
    }

    @Override
    public int createSubtask(Subtask subtask) throws IOException, TaskNotFound {
        super.createSubtask(subtask);
        save(subtask);
        int epicId = subtask.getEpicId();
        update(String.valueOf(epicId), getEpic(epicId).toDataStorageFile());
        return subtask.getTaskId();
    }

    @Override
    public void deleteSubtasks() throws IOException {
        super.deleteSubtasks();
        delete(1, "SUBTASK");
    }

    @Override
    public void deleteSubtask(int id) throws IOException, TaskNotFound {
        super.deleteSubtask(id);
        delete(0, String.valueOf(id));
    }

    @Override
    public Integer updateSubtask(Subtask updatedSubtask) throws IOException, TaskNotFound {
        super.updateSubtask(updatedSubtask);
        int subtaskId = updatedSubtask.getTaskId();
        update(String.valueOf(subtaskId), updatedSubtask.toDataStorageFile());
        return subtaskId;
    }

    @Override
    public int createEpic(Epic epic) throws IOException {
        super.createEpic(epic);
        save(epic);
        return epic.getTaskId();
    }

    @Override
    public void deleteEpics() throws IOException {
        super.deleteEpics();
        delete(1, "EPIC");
    }

    @Override
    public void deleteEpic(int id) throws IOException, TaskNotFound {
        super.deleteEpic(id);
        delete(0, String.valueOf(id));
    }

    @Override
    public Integer updateEpic(Epic updatedEpic) throws IOException, TaskNotFound {
        super.updateEpic(updatedEpic);
        int epicId = updatedEpic.getTaskId();
        update(String.valueOf(epicId), updatedEpic.toDataStorageFile());
        return epicId;
    }
}

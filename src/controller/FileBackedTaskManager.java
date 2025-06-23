package controller;

import exceptions.ManagerSaveException;
import model.Epic;
import model.Subtask;
import model.Task;
import util.TaskStatus;

import java.io.BufferedWriter;
import java.io.BufferedReader;
import java.nio.charset.StandardCharsets;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Comparator;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final Path fileStoragePath;

    public FileBackedTaskManager() throws IOException {
        super();
        this.fileStoragePath = Paths.get("src", "resources", "dataStorage.csv");
        initiateFileStorage();
    }

    public FileBackedTaskManager(Path fileStoragePath) throws IOException {
        super();
        this.fileStoragePath = fileStoragePath;
        initiateFileStorage();
    }

    private void initiateFileStorage() {
        if (!Files.exists(fileStoragePath)) {
            try {
                Files.createFile(fileStoragePath);
                try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileStoragePath.toFile()))) {
                    writer.write("id,type,name,status,description,startTime,duration,endTime,epic");
                    writer.newLine();
                }
            } catch (IOException e) {
                throw new ManagerSaveException("Ошибка при создании файла с данными");
            }
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        if (file == null || !file.exists()) {
            throw new ManagerSaveException("Файл " + file + " отсутствует или не может быть открыт");
        }
        try {
            FileBackedTaskManager manager = new FileBackedTaskManager(file.toPath());
            ArrayList<String> lines = new ArrayList<>();
            try (BufferedReader reader = Files.newBufferedReader(file.toPath(), StandardCharsets.UTF_8)) {
                String line;
                while ((line = reader.readLine()) != null) {
                    if (!line.equals("id,type,name,status,description,startTime,duration,endTime,epic")) {
                        lines.add(line);
                    }
                }
            }
            // Сортировка, чтобы сабтаски создавались сразу с нужными эпиками
            lines.sort(Comparator.comparingInt(s -> {
                String type = s.split(",")[1];
                return switch (type) {
                    case "TASK" -> 0;
                    case "EPIC" -> 1;
                    case "SUBTASK" -> 2;
                    default -> 3; // Заглушка чтобы был default
                };
            }));
            if (!lines.isEmpty()) {
                for (String line : lines) {
                    String[] fields = line.split(",", -1);
                    int id = Integer.parseInt(fields[0]);
                    String type = fields[1];
                    String name = fields[2];
                    TaskStatus status = TaskStatus.valueOf(fields[3]);
                    String description = fields[4];
                    String startTimeFromFile = fields[5];
                    String durationFromFile = fields[6];
                    String epicIdFromFile = fields[8];

                    DateTimeFormatter inputFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");
                    switch (type) {
                        case "TASK" -> {
                            Task task;
                            if (!startTimeFromFile.isEmpty()) {
                                task = new Task(name, description, id, status, LocalDateTime.parse(startTimeFromFile, inputFormatter), Duration.parse(durationFromFile));
                            } else {
                                task = new Task(name, description, id, status);
                            }
                            manager.tasks.put(id, task);
                            manager.taskStorage.put(id, task);
                        }
                        case "EPIC" -> {
                            Epic epic = new Epic(name, description, id, status);
                            manager.epics.put(id, epic);
                            manager.taskStorage.put(id, epic);
                        }
                        case "SUBTASK" -> {
                            int epicId = Integer.parseInt(epicIdFromFile);
                            Subtask subtask;
                            if (!startTimeFromFile.isEmpty()) {
                                subtask = new Subtask(name, description, id, status, LocalDateTime.parse(startTimeFromFile, inputFormatter), Duration.parse(durationFromFile), epicId);
                            } else {
                                subtask = new Subtask(name, description, id, epicId, status);
                            }

                            manager.getEpic(epicId).addSubtask(subtask);

                            manager.subtasks.put(id, subtask);
                            manager.taskStorage.put(id, subtask);
                        }
                    }
                }
            }
            return manager;
        } catch (IOException e) {
            throw new ManagerSaveException("Возникла ошибка при получении данных из файла.");
        }
    }


    public Path getFileStoragePath() {
        return fileStoragePath;
    }

    private void save() {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(fileStoragePath.toFile()))) {
            String header = "id,type,name,status,description,startTime,duration,endTime,epic";
            writer.write(header);
            writer.newLine();
            for (Task task : getTaskStorage().values()) {
                writer.write(task.toDataStorageFile());
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения данных");
        }
    }

    @Override
    public int createTask(Task task) {
        super.createTask(task);
        save();
        return task.getTaskId();
    }

    @Override
    public void deleteTasks() {
        super.deleteTasks();
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public Integer updateTask(Task updatedTask) {
        super.updateTask(updatedTask);
        int taskId = updatedTask.getTaskId();
        save();
        return taskId;
    }

    @Override
    public int createSubtask(Subtask subtask) {
        super.createSubtask(subtask);
        save();
        return subtask.getTaskId();
    }

    @Override
    public void deleteSubtasks() {
        super.deleteSubtasks();
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public Integer updateSubtask(Subtask updatedSubtask) {
        super.updateSubtask(updatedSubtask);
        save();
        return updatedSubtask.getTaskId();
    }

    @Override
    public int createEpic(Epic epic) {
        super.createEpic(epic);
        save();
        return epic.getTaskId();
    }

    @Override
    public void deleteEpics() {
        super.deleteEpics();
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public Integer updateEpic(Epic updatedEpic) {
        super.updateEpic(updatedEpic);
        save();
        return updatedEpic.getTaskId();
    }
}

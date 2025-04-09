package controller;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class TaskManager {
    private final HashMap<Integer, Object> taskStorage = new HashMap<>();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int idCounter = 0;

    public HashMap<Integer, Object> getTaskStorage() {
        return taskStorage;
    }

    // Методы для Tasks
    public int createTask(Task task) {
        final int id = ++idCounter;
        task.setTaskId(id);
        tasks.put(id, task);
        taskStorage.put(id, task);
        return id;
    }

    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    public Task getTask(int id) {
        return tasks.get(id);
    }

    public void deleteTasks() {
        List<Integer> taskIds = tasks.values().stream()
                .map(Task::getTaskId)
                .toList();
        tasks.clear();
        taskIds.forEach(taskStorage::remove);
    }

    public void deleteTask(int id) {
        tasks.remove(id);
        taskStorage.remove(id);
    }

    public Integer updateTask(Task updatedTask) {
        Task task = tasks.get(updatedTask.getTaskId());
        if (task != null) {
            if (task.equals(updatedTask)) {
                int taskId = task.getTaskId();
                tasks.put(taskId, updatedTask);
                taskStorage.put(taskId, updatedTask);
                return taskId;
            }
        }
        throw new UnsupportedOperationException("Задача с ID: " + updatedTask.getTaskId()
                + " отсутствует. Воспользуется методом createTask");
    }


    // Методы для Subtasks
    public int createSubtask(Subtask subtask) {
        final int id = ++idCounter;
        subtask.setTaskId(id);
        subtasks.put(id, subtask);
        taskStorage.put(id, subtask);
        Epic subtaskEpic = epics.get(subtask.getEpicId());
        subtaskEpic.addSubtask(subtask);
        subtaskEpic.manageEpicStatus();
        return id;
    }

    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    public Subtask getSubtask(int id) {
        return subtasks.get(id);
    }

    public void deleteSubtasks() {
        List<Integer> subtaskIds = subtasks.values().stream()
                .map(epic -> epic.getTaskId())
                .collect(Collectors.toList());
        subtasks.values().forEach(subtask -> {
            Epic epic = epics.get(subtask.getEpicId());
            epic.removeSubtask(subtask);
            epic.manageEpicStatus();
        });
        subtasks.clear();
        subtaskIds.forEach(taskStorage::remove);
    }

    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(subtask);
        epic.manageEpicStatus();
        subtasks.remove(id);
        taskStorage.remove(id);
    }


    public Integer updateSubtask(Subtask updatedSubtask) {
        Subtask subtask = subtasks.get(updatedSubtask.getTaskId());
        if (subtask.equals(updatedSubtask)) {
            int taskId = subtask.getTaskId();
            subtasks.put(taskId, updatedSubtask);
            taskStorage.put(taskId, updatedSubtask);
            Epic epic = epics.get(subtask.getEpicId());
            epic.removeSubtask(subtask);
            epic.addSubtask(updatedSubtask);
            epic.manageEpicStatus();
            return taskId;
        }
        throw new UnsupportedOperationException("Задача с ID: " + updatedSubtask.getTaskId()
                + " отсутствует. Воспользуется методом createSubtask");
    }


    // Методы для Epics
    public int createEpic(Epic epic) {
        final int id = ++idCounter;
        epic.setTaskId(id);
        epics.put(id, epic);
        taskStorage.put(id, epic);
        return id;
    }

    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    public Epic getEpic(int id) {
        return epics.get(id);
    }

    public void deleteEpics() {
        List<Integer> epicIds = epics.values().stream()
                .map(epic -> epic.getTaskId())
                .collect(Collectors.toList());
        epics.clear();
        epicIds.forEach(taskStorage::remove);
    }

    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        ArrayList<Subtask> epicSubtasks = epic.getSubtasks();
        epicSubtasks.forEach(epicSubtask -> {
            int taskId = epicSubtask.getTaskId();
            subtasks.remove(taskId);
            taskStorage.remove(taskId);
        });
        epics.remove(id);
        taskStorage.remove(id);
    }

    public Integer updateEpic(Epic updatedEpic) {
        Epic epic = epics.get(updatedEpic.getTaskId());
        if (epic.equals(updatedEpic)) {
            int epicId = epic.getTaskId();
            tasks.put(epicId, updatedEpic);
            taskStorage.put(epicId, updatedEpic);
            return epicId;
        }
        throw new UnsupportedOperationException("Задача с ID: " + updatedEpic.getTaskId()
                + " отсутствует. Воспользуется методом createEpic");
    }
}
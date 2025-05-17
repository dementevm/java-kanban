package controller;

import model.Epic;
import model.Subtask;
import model.Task;
import util.Managers;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> taskStorage = new HashMap<>();
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private int idCounter = 0;
    private final HistoryManager historyManager = Managers.getDefaultHistory();

    @Override
    public HashMap<Integer, Task> getTaskStorage() {
        return taskStorage;
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    // Методы для Tasks
    @Override
    public int createTask(Task task) {
        final int id = ++idCounter;
        task.setTaskId(id);
        tasks.put(id, task);
        taskStorage.put(id, task);
        return id;
    }

    @Override
    public ArrayList<Task> getTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public Task getTask(int id) {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
            return task;
        }
        return null;
    }

    @Override
    public void deleteTasks() {
        List<Integer> taskIds = tasks.values().stream().map(Task::getTaskId).toList();
        tasks.clear();
        taskIds.forEach(taskStorage::remove);
        taskIds.forEach(historyManager::remove);
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
        taskStorage.remove(id);
        historyManager.remove(id);
    }

    @Override
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
        throw new UnsupportedOperationException("Задача с ID: " + updatedTask.getTaskId() + " отсутствует. Воспользуется методом createTask");
    }


    // Методы для Subtasks
    @Override
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

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
            return subtask;
        }
        return null;
    }

    @Override
    public void deleteSubtasks() {
        List<Integer> subtaskIds = subtasks.values().stream().map(epic -> epic.getTaskId()).collect(Collectors.toList());
        subtasks.values().forEach(subtask -> {
            Epic epic = epics.get(subtask.getEpicId());
            epic.removeSubtask(subtask);
            epic.manageEpicStatus();
        });
        subtasks.clear();
        subtaskIds.forEach(taskStorage::remove);
        subtaskIds.forEach(historyManager::remove);
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        Epic epic = epics.get(subtask.getEpicId());
        epic.removeSubtask(subtask);
        epic.manageEpicStatus();
        subtasks.remove(id);
        taskStorage.remove(id);
        historyManager.remove(id);
    }


    @Override
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
        throw new UnsupportedOperationException("Задача с ID: " + updatedSubtask.getTaskId() + " отсутствует. Воспользуется методом createSubtask");
    }


    // Методы для Epics
    @Override
    public int createEpic(Epic epic) {
        final int id = ++idCounter;
        epic.setTaskId(id);
        epics.put(id, epic);
        taskStorage.put(id, epic);
        return id;
    }

    @Override
    public ArrayList<Epic> getEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public Epic getEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
            return epic;
        }
        return null;
    }

    @Override
    public void deleteEpics() {
        List<Integer> epicIds = epics.values().stream().map(Task::getTaskId).toList();
        epicIds.forEach(id ->  {
            Epic epic = epics.get(id);
            ArrayList<Subtask> epicsSubtasks = epic.getSubtasks();
            if (!epicsSubtasks.isEmpty()) {
                epicsSubtasks.forEach(subtask -> {
                    subtasks.remove(subtask.getTaskId());
                    taskStorage.remove(subtask.getTaskId());
                    historyManager.remove(subtask.getTaskId());
                });
            }
        });
        epics.clear();
        epicIds.forEach(taskStorage::remove);
        epicIds.forEach(historyManager::remove);
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        ArrayList<Subtask> epicSubtasks = epic.getSubtasks();
        epicSubtasks.forEach(epicSubtask -> {
            int taskId = epicSubtask.getTaskId();
            subtasks.remove(taskId);
            taskStorage.remove(taskId);
            historyManager.remove(taskId);
        });
        epics.remove(id);
        taskStorage.remove(id);
        historyManager.remove(id);
    }

    @Override
    public Integer updateEpic(Epic updatedEpic) {
        Epic epic = epics.get(updatedEpic.getTaskId());
        if (epic.equals(updatedEpic)) {
            int epicId = epic.getTaskId();
            epics.put(epicId, updatedEpic);
            taskStorage.put(epicId, updatedEpic);
            return epicId;
        }
        throw new UnsupportedOperationException("Задача с ID: " + updatedEpic.getTaskId() + " отсутствует. Воспользуется методом createEpic");
    }
}
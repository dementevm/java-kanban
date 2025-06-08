package controller;

import exceptions.TaskNotFound;
import model.Epic;
import model.Subtask;
import model.Task;
import util.Managers;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.stream.Collectors;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> taskStorage = new HashMap<>();
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
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
    public int createTask(Task task) throws IOException {
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
    public Task getTask(int id) throws TaskNotFound {
        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
            return task;
        }
        throw new TaskNotFound();
    }

    @Override
    public void deleteTasks() throws IOException {
        List<Integer> taskIds = tasks.values().stream().map(Task::getTaskId).toList();
        tasks.clear();
        taskIds.forEach(taskStorage::remove);
        taskIds.forEach(historyManager::remove);
    }

    @Override
    public void deleteTask(int id) throws IOException, TaskNotFound {
        if (tasks.containsKey(id)) {
            tasks.remove(id);
            taskStorage.remove(id);
            historyManager.remove(id);
        } else {
            throw new TaskNotFound();
        }
    }

    @Override
    public Integer updateTask(Task updatedTask) throws IOException, TaskNotFound {
        Task task = tasks.get(updatedTask.getTaskId());
        if (task != null) {
            if (task.equals(updatedTask)) {
                int taskId = task.getTaskId();
                tasks.put(taskId, updatedTask);
                taskStorage.put(taskId, updatedTask);
                return taskId;
            }
        }
        throw new TaskNotFound("Задача с ID: " + updatedTask.getTaskId() + " отсутствует. Воспользуется методом createTask");
    }


    // Методы для Subtasks
    @Override
    public int createSubtask(Subtask subtask) throws IOException, TaskNotFound {
        final int id = ++idCounter;
        Epic subtaskEpic = epics.get(subtask.getEpicId());
        if (subtaskEpic != null) {
            subtask.setTaskId(id);
            subtasks.put(id, subtask);
            taskStorage.put(id, subtask);
            subtaskEpic.addSubtask(subtask);
            subtaskEpic.manageEpicStatus();
            return id;
        } else {
            throw new TaskNotFound();
        }
    }

    @Override
    public ArrayList<Subtask> getSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public Subtask getSubtask(int id) throws TaskNotFound {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
            return subtask;
        }
        throw new TaskNotFound();
    }

    @Override
    public void deleteSubtasks() throws IOException {
        List<Integer> subtaskIds = subtasks.values().stream().map(Task::getTaskId).toList();
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
    public void deleteSubtask(int id) throws IOException, TaskNotFound {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(subtask);
                epic.manageEpicStatus();
            }
            subtasks.remove(id);
            taskStorage.remove(id);
            historyManager.remove(id);
        } else {
            throw new TaskNotFound();
        }
    }


    @Override
    public Integer updateSubtask(Subtask updatedSubtask) throws IOException, TaskNotFound {
        Subtask subtask = subtasks.get(updatedSubtask.getTaskId());
        if (subtask != null) {
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
        }
        throw new TaskNotFound("Задача с ID: " + updatedSubtask.getTaskId() + " отсутствует. Воспользуется методом createSubtask");
    }


    // Методы для Epics
    @Override
    public int createEpic(Epic epic) throws IOException {
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
    public Epic getEpic(int id) throws TaskNotFound {
        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
            return epic;
        }
        throw new TaskNotFound();
    }

    @Override
    public void deleteEpics() throws IOException {
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
    public void deleteEpic(int id) throws IOException, TaskNotFound {
        Epic epic = epics.get(id);
        if (epic != null) {
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
        } else {
            throw new TaskNotFound();
        }

    }

    @Override
    public Integer updateEpic(Epic updatedEpic) throws IOException, TaskNotFound {
        Epic epic = epics.get(updatedEpic.getTaskId());
        if (epic != null) {
            if (epic.equals(updatedEpic)) {
                updatedEpic.clearSubtasks();
                int epicId = epic.getTaskId();
                for (Subtask subtask : epic.getSubtasks()) {
                    updatedEpic.addSubtask(subtask);
                }
                epics.put(epicId, updatedEpic);
                taskStorage.put(epicId, updatedEpic);
                return epicId;
            }
        }
        throw new TaskNotFound();
    }
}
package controller;

import exceptions.TaskCreateError;
import exceptions.TaskNotFound;
import model.Epic;
import model.Subtask;
import model.Task;
import util.Managers;

import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> taskStorage = new HashMap<>();
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final TreeSet<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime,
                    Comparator.nullsLast(Comparator.naturalOrder())));
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
        if (hasTimeIntersection(task)) {
            throw new TaskCreateError("У задачи есть пересечение по времени выполнения с существующей задачей");
        }
        final int id = ++idCounter;
        task.setTaskId(id);
        tasks.put(id, task);
        taskStorage.put(id, task);
        prioritizedTasks.add(task);
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
        throw new TaskNotFound();
    }

    @Override
    public void deleteTasks() {
        List<Integer> taskIds = tasks.values().stream().map(Task::getTaskId).toList();
        tasks.values().forEach(prioritizedTasks::remove);
        tasks.clear();
        taskIds.forEach(taskStorage::remove);
        taskIds.forEach(historyManager::remove);
    }

    @Override
    public void deleteTask(int id) {
        if (tasks.containsKey(id)) {
            prioritizedTasks.remove(tasks.get(id));
            tasks.remove(id);
            taskStorage.remove(id);
            historyManager.remove(id);
        } else {
            throw new TaskNotFound();
        }
    }

    @Override
    public Integer updateTask(Task updatedTask) {
        if (hasTimeIntersection(updatedTask)) {
            throw new TaskCreateError("У задачи есть пересечение по времени выполнения с существующей задачей");
        }
        Task task = tasks.get(updatedTask.getTaskId());
        if (task != null && task.equals(updatedTask)) {
            prioritizedTasks.remove(updatedTask);
            prioritizedTasks.add(updatedTask);
            int taskId = task.getTaskId();
            tasks.put(taskId, updatedTask);
            taskStorage.put(taskId, updatedTask);
            return taskId;
        }
        throw new TaskNotFound("Задача с ID: " + updatedTask.getTaskId() + " отсутствует. Воспользуется методом createTask");
    }

    // Методы для Subtasks
    @Override
    public int createSubtask(Subtask subtask) {
        if (hasTimeIntersection(subtask)) {
            throw new TaskCreateError("У задачи есть пересечение по времени выполнения с существующей задачей");
        }
        Epic subtaskEpic = epics.get(subtask.getEpicId());
        if (subtaskEpic != null) {
            final int id = ++idCounter;
            subtask.setTaskId(id);
            subtasks.put(id, subtask);
            taskStorage.put(id, subtask);
            prioritizedTasks.add(subtask);
            subtaskEpic.addSubtask(subtask);
            subtaskEpic.manageEpicStatus();
            subtaskEpic.setEndTime();
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
    public Subtask getSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
            return subtask;
        }
        throw new TaskNotFound();
    }

    @Override
    public void deleteSubtasks() {
        List<Integer> subtaskIds = subtasks.values().stream().map(Task::getTaskId).toList();
        subtasks.values().forEach(subtask -> {
            prioritizedTasks.remove(subtask);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(subtask);
                epic.manageEpicStatus();
                epic.setEndTime();
            }
        });
        subtasks.clear();
        subtaskIds.forEach(taskStorage::remove);
        subtaskIds.forEach(historyManager::remove);
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(subtask);
                epic.manageEpicStatus();
                epic.setEndTime();
            }
            prioritizedTasks.remove(subtask);
            subtasks.remove(id);
            taskStorage.remove(id);
            historyManager.remove(id);
        } else {
            throw new TaskNotFound();
        }
    }

    @Override
    public Integer updateSubtask(Subtask updatedSubtask) {
        if (hasTimeIntersection(updatedSubtask)) {
            throw new TaskCreateError("У задачи есть пересечение по времени выполнения с существующей задачей");
        }
        Subtask subtask = subtasks.get(updatedSubtask.getTaskId());
        if (subtask != null && subtask.equals(updatedSubtask)) {
            int taskId = subtask.getTaskId();
            prioritizedTasks.remove(subtask);
            prioritizedTasks.add(updatedSubtask);
            subtasks.put(taskId, updatedSubtask);
            taskStorage.put(taskId, updatedSubtask);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtask(subtask);
                epic.addSubtask(updatedSubtask);
                epic.manageEpicStatus();
                epic.setEndTime();
            }
            return taskId;
        }
        throw new TaskNotFound("Задача с ID: " + updatedSubtask.getTaskId() + " отсутствует. Воспользуется методом createSubtask");
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
        throw new TaskNotFound();
    }

    @Override
    public void deleteEpics() {
        List<Integer> epicIds = epics.values().stream().map(Task::getTaskId).toList();
        epicIds.forEach(id -> {
            Epic epic = epics.get(id);
            if (epic != null) {
                ArrayList<Subtask> epicsSubtasks = epic.getSubtasks();
                if (!epicsSubtasks.isEmpty()) {
                    epicsSubtasks.forEach(subtask -> {
                        subtasks.remove(subtask.getTaskId());
                        taskStorage.remove(subtask.getTaskId());
                        historyManager.remove(subtask.getTaskId());
                    });
                }
            }
        });
        epics.clear();
        epicIds.forEach(taskStorage::remove);
        epicIds.forEach(historyManager::remove);
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.get(id);
        if (epic != null) {
            ArrayList<Subtask> epicSubtasks = epic.getSubtasks();
            if (epicSubtasks != null && !epicSubtasks.isEmpty()) {
                epicSubtasks.forEach(epicSubtask -> {
                    int taskId = epicSubtask.getTaskId();
                    subtasks.remove(taskId);
                    taskStorage.remove(taskId);
                    historyManager.remove(taskId);
                });
            }
            epics.remove(id);
            taskStorage.remove(id);
            historyManager.remove(id);
        } else {
            throw new TaskNotFound();
        }
    }

    @Override
    public Integer updateEpic(Epic updatedEpic) {
        Epic epic = epics.get(updatedEpic.getTaskId());
        if (epic != null && epic.equals(updatedEpic)) {
            updatedEpic.clearSubtasks();
            int epicId = epic.getTaskId();
            for (Subtask subtask : epic.getSubtasks()) {
                updatedEpic.addSubtask(subtask);
            }
            epics.put(epicId, updatedEpic);
            taskStorage.put(epicId, updatedEpic);
            return epicId;
        }
        throw new TaskNotFound();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    @Override
    public boolean hasTimeIntersection(Task newTask) {
        if (newTask.getStartTime() == null) {
            return false;
        }

        prioritizedTasks.remove(newTask);
        return prioritizedTasks.stream()
                .anyMatch(task ->
                        task.getStartTime().isBefore(newTask.getEndTime()) &&
                                newTask.getStartTime().isBefore(task.getEndTime())
                );
    }
}

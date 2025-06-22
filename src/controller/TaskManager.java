package controller;

import model.Epic;
import model.Subtask;
import model.Task;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.TreeSet;

public interface TaskManager {
    HashMap<Integer, Task> getTaskStorage();

    List<Task> getHistory();

    // Методы для Tasks
    int createTask(Task task);

    ArrayList<Task> getTasks();

    Task getTask(int id);

    void deleteTasks();

    void deleteTask(int id);

    Integer updateTask(Task updatedTask);

    // Методы для Subtasks
    int createSubtask(Subtask subtask);

    ArrayList<Subtask> getSubtasks();

    Subtask getSubtask(int id);

    void deleteSubtasks();

    void deleteSubtask(int id);

    Integer updateSubtask(Subtask updatedSubtask);

    // Методы для Epics
    int createEpic(Epic epic);

    ArrayList<Epic> getEpics();

    Epic getEpic(int id);

    void deleteEpics();

    void deleteEpic(int id);

    Integer updateEpic(Epic updatedEpic);

    TreeSet<Task> getPrioritizedTasks();

    boolean hasTimeIntersection(Task task);
}


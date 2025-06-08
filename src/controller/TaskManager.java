package controller;

import exceptions.TaskNotFound;
import model.Epic;
import model.Subtask;
import model.Task;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public interface TaskManager {
    HashMap<Integer, Task> getTaskStorage();

    List<Task> getHistory();

    // Методы для Tasks
    int createTask(Task task) throws IOException;

    ArrayList<Task> getTasks();

    Task getTask(int id) throws TaskNotFound;

    void deleteTasks() throws IOException;

    void deleteTask(int id) throws IOException, TaskNotFound;

    Integer updateTask(Task updatedTask) throws IOException, TaskNotFound;

    // Методы для Subtasks
    int createSubtask(Subtask subtask) throws IOException, TaskNotFound;

    ArrayList<Subtask> getSubtasks();

    Subtask getSubtask(int id) throws TaskNotFound;

    void deleteSubtasks() throws IOException;

    void deleteSubtask(int id) throws IOException, TaskNotFound;

    Integer updateSubtask(Subtask updatedSubtask) throws IOException, TaskNotFound;

    // Методы для Epics
    int createEpic(Epic epic) throws IOException;

    ArrayList<Epic> getEpics();

    Epic getEpic(int id) throws TaskNotFound;

    void deleteEpics() throws IOException;

    void deleteEpic(int id) throws IOException, TaskNotFound;

    Integer updateEpic(Epic updatedEpic) throws IOException, TaskNotFound;
}


package model;

import util.TaskStatus;

import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Subtask> epicSubtasks = new ArrayList<>();

    public Epic(String taskName, String description) {
        super(taskName, description);
        manageEpicStatus();
    }

    public Epic(String taskName, String description, int id) {
        super(taskName, description, id);
        manageEpicStatus();
    }

    public Epic(String taskName, String description, int id, TaskStatus status) {
        // Конструктор для добавления эпиков из файла.
        super(taskName, description, id, status);
    }

    public ArrayList<Subtask> getSubtasks() {
        return epicSubtasks;
    }

    public void addSubtask(Subtask subtask) {
        epicSubtasks.add(subtask);
    }

    public void removeSubtask(Subtask subtask) {
        epicSubtasks.remove(subtask);
    }

    public void manageEpicStatus() {
        if (this.epicSubtasks.isEmpty()) {
            this.status = TaskStatus.NEW;
            return;
        }

        boolean allNew = epicSubtasks.stream()
                .map(Task::getStatus)
                .allMatch(status -> status == TaskStatus.NEW);
        if (allNew) {
            this.status = TaskStatus.NEW;
            return;
        }
        boolean allDone = epicSubtasks.stream()
                .map(Task::getStatus)
                .allMatch(status -> status == TaskStatus.DONE);

        if (allDone) {
            this.status = TaskStatus.DONE;
            return;
        }

        this.status = TaskStatus.IN_PROGRESS;
    }

    public void clearSubtasks() {
        epicSubtasks.clear();
    }

    @Override
    public void setStatus(TaskStatus status) {
        throw new UnsupportedOperationException("Статус у задачи типа EPIC контролируется автоматически.");
    }

    @Override
    public String toString() {
        return "model.Epic{" +
                "taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", taskId=" + taskId +
                ", status='" + status + '\'' +
                '}';
    }
}

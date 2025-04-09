package model;

import util.TaskStatus;
import java.util.ArrayList;

public class Epic extends Task {
    protected ArrayList<Subtask> epicSubtasks;

    public Epic(String taskName, String description) {
        super(taskName, description);
        epicSubtasks = new ArrayList<>();
        manageEpicStatus();
    }

    public Epic(String taskName, String description, int id) {
        super(taskName, description, id);
        epicSubtasks = new ArrayList<>();
        manageEpicStatus();
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

    public ArrayList<Subtask> getEpicSubtasks() {
        return epicSubtasks;
    }

    public void manageEpicStatus() {
        if (this.epicSubtasks.isEmpty()) {
            this.status = TaskStatus.NEW;
            return;
        }

        boolean allNew =  epicSubtasks.stream()
                .map(subtask -> subtask.getStatus())
                .allMatch(status -> status == TaskStatus.NEW);
        if (allNew) {
            this.status = TaskStatus.NEW;
            return;
        }
        boolean allDone = epicSubtasks.stream()
                .map(subtask -> subtask.getStatus())
                .allMatch(status -> status == TaskStatus.DONE);

        if (allDone) {
            this.status = TaskStatus.DONE;
            return;
        }

        this.status = TaskStatus.IN_PROGRESS;
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
                ", status=" + status +
                '}';
    }
}

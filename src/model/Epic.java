package model;

import java.time.LocalDateTime;
import java.time.Duration;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.Objects;

import util.TaskStatus;

public class Epic extends Task {
    protected ArrayList<Subtask> epicSubtasks = new ArrayList<>();
    protected LocalDateTime endTime;

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

    private Duration getTotalSubtaskDuration() {
        return this.epicSubtasks.stream()
                .map(subtask -> subtask.getDuration() == null ? Duration.ZERO : subtask.getDuration())
                .reduce(Duration.ZERO, Duration::plus);
    }

    @Override
    public LocalDateTime getStartTime() {
        return getFirstSubtaskStartTime();
    }

    @Override
    public Duration getDuration() {
        return getTotalSubtaskDuration();
    }

    private LocalDateTime getFirstSubtaskStartTime() {
        return this.epicSubtasks.stream()
                .map(Subtask::getStartTime)
                .filter(Objects::nonNull)
                .min(Comparator.naturalOrder())
                .orElse(null);
    }

    private LocalDateTime getLastSubtaskEndTime() {
        return this.epicSubtasks.stream()
                .map(Subtask::getEndTime)
                .filter(Objects::nonNull)
                .max(Comparator.naturalOrder())
                .orElse(null);

    }

    public void setEndTime() {
        LocalDateTime endTime = getLastSubtaskEndTime();
        if (endTime != null) {
            this.endTime = endTime;
        }
    }

    @Override
    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void clearSubtasks() {
        epicSubtasks.clear();
        manageEpicStatus();
    }

    @Override
    public void setStatus(TaskStatus status) {
        throw new UnsupportedOperationException("Статус у задачи типа EPIC контролируется автоматически.");
    }

    @Override
    public String toString() {
        LocalDateTime epicStartTime = this.getFirstSubtaskStartTime();
        Duration epicTotalDuration = this.getTotalSubtaskDuration();

        String epicStartTimeToString = (epicStartTime != null) ? epicStartTime.format(formatter) : "<не задано>";
        String epicDurationToString = (!epicTotalDuration.equals(Duration.ZERO)) ? epicTotalDuration.toHoursPart() + ":" + epicTotalDuration.toMinutesPart() : "<не задано>";
        String epicEndTimeToString = (epicStartTime != null) ? getEndTime().format(formatter) : "<не задано>";

        return "model.Epic{" +
                "taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", taskId=" + taskId +
                ", status='" + status + '\'' +
                ", startTime=" + epicStartTimeToString +
                ", duration=" + epicDurationToString +
                ", endTime=" + epicEndTimeToString  +
                '}';
    }
}

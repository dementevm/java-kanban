package model;

import com.google.gson.annotations.Expose;
import util.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Objects;
import java.util.Optional;

public class Task {
    @Expose
    protected String taskName;
    @Expose
    protected String description;
    @Expose
    protected int taskId;
    @Expose
    protected TaskStatus status = TaskStatus.NEW;
    @Expose
    protected LocalDateTime startTime;
    @Expose
    protected Duration duration;
    protected static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm");


    public Task(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
    }

    public Task(String taskName, String description, int taskId) {
        this.taskName = taskName;
        this.description = description;
        this.taskId = taskId;
    }

    public Task(String taskName, String description, TaskStatus status) {
        this.taskName = taskName;
        this.description = description;
        this.status = status;
    }

    public Task(String taskName, String description, int taskId, TaskStatus status) {
        this.taskName = taskName;
        this.description = description;
        this.status = status;
        this.taskId = taskId;
    }

    public Task(String taskName, String description, LocalDateTime startTime, Duration duration) {
        this.taskName = taskName;
        this.description = description;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String taskName, String description, int taskId, LocalDateTime startTime, Duration duration) {
        this.taskName = taskName;
        this.description = description;
        this.taskId = taskId;
        this.startTime = startTime;
        this.duration = duration;
    }

    public Task(String taskName, String description, int taskId, TaskStatus status, LocalDateTime startTime, Duration duration) {
        this.taskName = taskName;
        this.description = description;
        this.taskId = taskId;
        this.status = status;
        this.startTime = startTime;
        this.duration = duration;
    }

    public String getTaskName() {
        return taskName;
    }

    public void setTaskName(String taskName) {
        this.taskName = taskName;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getTaskId() {
        return taskId;
    }

    public void setTaskId(int taskId) {
        this.taskId = taskId;
    }

    public TaskStatus getStatus() {
        return status;
    }

    public void setStatus(TaskStatus status) {
        this.status = status;
    }

    public Duration getDuration() {
        return duration;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public LocalDateTime getEndTime() {
        if (startTime != null) {
            return startTime.plus(duration);
        }
        return null;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return taskId == task.taskId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(taskId);
    }

    @Override
    public String toString() {
        String taskTimeToString = (startTime != null) ? startTime.format(formatter) : "<не задано>";
        String taskDurationToString = (duration != null) ? duration.toHoursPart() + ":" + duration.toMinutesPart() : "<не задано>";
        return "model.Task{" +
                "taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", taskId=" + taskId +
                ", startTime=" + taskTimeToString +
                ", duration=" + taskDurationToString +
                "}";
    }

    public String toDataStorageFile() {
        return taskId + "," + getClass().getSimpleName().toUpperCase() + "," + taskName + "," + status
                + "," + description + ","
                + Optional.ofNullable(startTime).map(Objects::toString).orElse("") + ","
                + Optional.ofNullable(duration).map(Objects::toString).orElse("")
                + "," + Optional.ofNullable(getEndTime()).map(Objects::toString).orElse("");
    }
}

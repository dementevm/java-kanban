package model;

import util.TaskStatus;

import java.util.Objects;

public class Task {
    protected String taskName;
    protected String description;
    protected int taskId;
    protected TaskStatus status = TaskStatus.NEW;

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
        return "model.Task{" +
                "taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", taskId=" + taskId +
                '}';
    }
}

package model;

import com.google.gson.annotations.Expose;
import util.TaskStatus;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    // Переменная Integer для реализации модификации статусов model.Epic'а
    @Expose
    private Integer epicId;

    public Subtask(String title, String description, int epicId) {
        super(title, description);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, int id, int epicId) {
        super(title, description, id);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, int epicId, TaskStatus status) {
        super(title, description);
        this.epicId = epicId;
        this.status = status;
    }

    public Subtask(String title, String description, int id, int epicId, TaskStatus status) {
        super(title, description, id, status);
        this.epicId = epicId;
    }

    public Subtask(String taskName, String description, LocalDateTime startTime, Duration duration, Integer epicId) {
        super(taskName, description, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String taskName, String description, int taskId, LocalDateTime startTime, Duration duration, Integer epicId) {
        super(taskName, description, taskId, startTime, duration);
        this.epicId = epicId;
    }

    public Subtask(String taskName, String description, int taskId, TaskStatus status, LocalDateTime startTime, Duration duration, Integer epicId) {
        super(taskName, description, taskId, status, startTime, duration);
        this.epicId = epicId;
    }

    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        String subtaskTimeToString = (startTime != null) ? startTime.format(formatter) : "<не задано>";
        String subtaskDurationToString = (duration != null) ? duration.toHoursPart() + ":" + duration.toMinutesPart() : "<не задано>";

        return "model.Subtask{" +
                "taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", taskId=" + taskId +
                ", epicId=" + epicId +
                ", startTime=" + subtaskTimeToString +
                ", duration=" + subtaskDurationToString +
                "}";
    }

    @Override
    public String toDataStorageFile() {
        return super.toDataStorageFile() + "," + epicId;
    }
}

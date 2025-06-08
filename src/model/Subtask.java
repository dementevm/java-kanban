package model;

import util.TaskStatus;

public class Subtask extends Task {
    // Переменная Integer для реализации модификации статусов model.Epic'а
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


    public Integer getEpicId() {
        return epicId;
    }

    public void setEpicId(int epicId) {
        this.epicId = epicId;
    }

    @Override
    public String toString() {
        return "model.Subtask{" +
                "taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status='" + status + '\'' +
                ", taskId=" + taskId +
                ", epicId=" + epicId +
                '}';
    }

    @Override
    public String toDataStorageFile() {
        return super.toDataStorageFile() + epicId;
    }
}

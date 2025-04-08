public class Subtask extends Task {
    // Переменная Integer для реализации модификации статусов Epic'а
    private Integer epicId;

    public Subtask(String taskName, String description, int epicId) {
        super(taskName, description);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, int id, int epicId) {
        super(title, description, id);
        this.epicId = epicId;
    }


    public Subtask(String title, String description, int id, int epicId, TaskStatus status) {
        super(title, description, id, status);
        this.epicId = epicId;
    }

    public Subtask(String title, String description, int epicId, TaskStatus status) {
        super(title, description, status);
        this.epicId = epicId;
    }

    public Subtask(String taskName, String description, int taskId, int epicId, TaskStatus status, boolean isUpdate) {
        super(taskName, description, taskId, status, isUpdate);
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
        return "Subtask{" +
                "taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status + '\'' +
                ", taskId=" + taskId +
                ", epicId=" + epicId +
                '}';
    }
}

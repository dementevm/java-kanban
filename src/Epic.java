import java.util.ArrayList;

public class Epic extends Task {

    public Epic(String taskName, String description) {
        super(taskName, description);
        manageEpicStatus(this);
    }

    public Epic(String taskName, String description, int taskId) {
        super(taskName, description, taskId);
        manageEpicStatus(this);
    }

    public Epic(String taskName, String description, int taskId, boolean isUpdate) {
        super(taskName, description, taskId, isUpdate);
        manageEpicStatus(this);
    }

    public static void manageEpicStatus(Epic epic) {
        ArrayList<Subtask> subtasks = TaskManager.getEpicSubtasks(epic.getTaskId());
        if (subtasks == null) {
            epic.status = TaskStatus.NEW;
        } else {
            ArrayList<TaskStatus> subtaskStatuses = new ArrayList<>();
            for (Subtask subtask : subtasks) {
                subtaskStatuses.add(subtask.getStatus());
            }

            boolean allNew = subtaskStatuses.stream()
                    .allMatch(subtaskStatus -> subtaskStatus.equals(TaskStatus.NEW));
            if (allNew) {
                epic.status = TaskStatus.NEW;
                return;
            }

            boolean allDone = subtaskStatuses.stream()
                    .allMatch(subtaskStatus -> subtaskStatus.equals(TaskStatus.DONE));
            if (allDone) {
                epic.status = TaskStatus.DONE;
                return;
            }

            epic.status = TaskStatus.IN_PROGRESS;
        }
    }

    @Override
    public void setStatus(TaskStatus status) {
        throw new UnsupportedOperationException("Статус у задачи типа EPIC контролируется автоматически.");
    }

    @Override
    public String toString() {
        return "Epic{" +
                "taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", taskId=" + taskId +
                ", status=" + status +
                '}';
    }
}

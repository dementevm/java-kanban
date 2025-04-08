import java.util.Objects;
import java.util.ArrayList;

public class Task {
    protected String taskName;
    protected String description;
    protected int taskId;
    protected TaskStatus status = TaskStatus.NEW;
    protected boolean isUpdating = false;
    private static int idCounter = 0;
    private static final ArrayList<Integer> uniqueIds = new ArrayList<>();

    public Task(String taskName, String description) {
        this.taskName = taskName;
        this.description = description;
        this.taskId = generateAutoId();
    }

    public Task(String taskName, String description, TaskStatus status) {
        this.taskName = taskName;
        this.description = description;
        this.taskId = generateAutoId();
        this.status = status;
    }

    public Task(String taskName, String description, int taskId) {
        this.taskName = taskName;
        this.description = description;
        this.taskId = checkIdAvailability(taskId);
    }

    public Task(String taskName, String description, int taskId, TaskStatus status) {
        this.taskName = taskName;
        this.description = description;
        this.status = status;
        this.taskId = checkIdAvailability(taskId);
    }

    public Task(String taskName, String description, int taskId, TaskStatus status, boolean isUpdate) {
        this.taskName = taskName;
        this.description = description;
        this.status = status;
        if (isUpdate) {
            this.taskId = taskId;
        } else {
            this.taskId = checkIdAvailability(taskId);
        }
    }

    public Task(String taskName, String description, int taskId, boolean isUpdate) {
        this.taskName = taskName;
        this.description = description;
        if (isUpdate) {
            this.taskId = taskId;
        } else {
            this.taskId = checkIdAvailability(taskId);
        }
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

    public void setIsUpdating(boolean isUpdating) {
        this.isUpdating = isUpdating;
    }

    private static int generateAutoId() {
        int nextId = idCounter + 1;
        while (uniqueIds.contains(nextId)) {
            nextId = nextId + 1;
        }
        idCounter = nextId;
        uniqueIds.add(nextId);
        return nextId;
    }

    private static int checkIdAvailability(int id) {
        if (uniqueIds.contains(id)) {
            throw new UnsupportedOperationException("Задачи с ID " + id + " Уже существует. - "
                    + TaskManager.getObjectById(id).toString() + ". Укажите другой ID");
        } else {
            uniqueIds.add(id);
            return id;
        }
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
        return Objects.hash(taskName, description, status, taskId);
    }

    @Override
    public String toString() {
        return "Task{" +
                "taskName='" + taskName + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status + '\'' +
                ", taskId=" + taskId +
                '}';
    }
}

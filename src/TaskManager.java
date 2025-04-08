import java.util.HashMap;
import java.util.ArrayList;

public class TaskManager {
    static final HashMap<Integer, Object> taskStorage = new HashMap<>();
    static final ArrayList<Task> tasks = new ArrayList<>();
    static final ArrayList<Subtask> subtasks = new ArrayList<>();
    static final ArrayList<Epic> epics = new ArrayList<>();

    // Методы для Tasks
    public static void createTask(Task newTask) {
        createObject(TaskType.TASK, newTask);
    }

    public static ArrayList<? extends Task> getTasks() {
        return getObjects(TaskType.TASK);
    }

    public static void deleteTasks() {
        deleteObjects(TaskType.TASK);
    }

    public static void updateTask(Task updatedTask) {
        for (Task task : tasks) {
            if (task.equals(updatedTask)) {
                tasks.set(tasks.indexOf(task), updatedTask);
                break;
            }
        }
        int taskId = updatedTask.getTaskId();
        if (taskStorage.containsKey(taskId)) {
            taskStorage.put(taskId, updatedTask);
        } else {
            System.out.println("Задача с ID: " + taskId + " отсутствует. Воспользуется методом createTask");
        }
    }


    // Методы для Subtasks
    public static void createSubtask(Subtask newSubtask) {
        createObject(TaskType.SUBTASK, newSubtask);
        Epic epic = (Epic) taskStorage.get(newSubtask.getEpicId());
        Epic.manageEpicStatus(epic);
    }

    public static ArrayList<? extends Task> getSubtasks() {
        return getObjects(TaskType.SUBTASK);
    }

    public static void deleteSubtasks() {
        ArrayList<Integer> taskIds = getIds(subtasks);
        for (Integer taskId : taskIds) {
            Subtask subtask = (Subtask) taskStorage.get(taskId);
            if (subtask.getEpicId() != null) {
                Epic epic = (Epic) taskStorage.get(subtask.getEpicId());
                Epic.manageEpicStatus(epic);
            }
            taskStorage.remove(taskId);
        }
        subtasks.clear();
    }

    public static void updateSubtask(Subtask updatedSubtask) {
        for (Subtask subtask : subtasks) {
            if (subtask.equals(updatedSubtask)) {
                subtasks.set(subtasks.indexOf(subtask), updatedSubtask);
                break;
            }
        }
        int subtaskId = updatedSubtask.getTaskId();
        if (taskStorage.containsKey(subtaskId)) {
            taskStorage.put(subtaskId, updatedSubtask);
            if (updatedSubtask.getEpicId() != null) {
                Epic epic = (Epic) taskStorage.get(updatedSubtask.getEpicId());
                Epic.manageEpicStatus(epic);
            }
        } else {
            System.out.println("Подзадача с ID: " + subtaskId + " отсутствует. Воспользуется методом createSubtask");
        }
    }


    // Методы для Epics
    public static void createEpic(Epic newEpic) {
        createObject(TaskType.EPIC, newEpic);
    }

    public static ArrayList<? extends Task> getEpics() {
        return getObjects(TaskType.EPIC);
    }

    public static void deleteEpics() {
        ArrayList<Integer> taskIds = getIds(epics);
        epics.clear();
        for (Integer taskId : taskIds) {
            taskStorage.remove(taskId);
        }
    }

    public static void updateEpic(Epic updatedEpic) {
        for (Epic epic : epics) {
            if (epic.equals(updatedEpic)) {
                epics.set(epics.indexOf(epic), updatedEpic);
                break;
            }
        }
        int epicId = updatedEpic.getTaskId();
        if (taskStorage.containsKey(epicId)) {
            taskStorage.put(epicId, updatedEpic);
        } else {
            System.out.println("Эпика с ID: " + epicId + " отсутствует. Воспользуется методом createEpic");
        }
    }

    public static ArrayList<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> epicSubtasks = new ArrayList<>();
        for (Subtask subtask : subtasks) {
            if (subtask.getEpicId() == epicId) {
                epicSubtasks.add(subtask);
            }
        }
        if (epicSubtasks.isEmpty()) {
            System.out.println("У эпика с ID: " + epicId + " отсутствуют подзадачи");
            return null;
        }
        return epicSubtasks;
    }

    // Общие
    public static void createObject(TaskType taskType, Task taskObject) {
        switch (taskType) {
            case TASK:
                tasks.add(taskObject);
                break;
            case SUBTASK:
                subtasks.add((Subtask) taskObject);
                break;
            case EPIC:
                epics.add((Epic) taskObject);
                break;
        }
        taskStorage.put(taskObject.getTaskId(), taskObject);
    }


    public static ArrayList<? extends Task> getObjects(TaskType taskType) {
        return switch (taskType) {
            case TASK -> tasks;
            case SUBTASK -> subtasks;
            case EPIC -> epics;
        };
    }

    public static Object getObjectById(int id) {
        if (taskStorage.containsKey(id)) {
            return taskStorage.get(id);
        }
        System.out.println("Отсутствует задание с id: " + id);
        return null;
    }

    public static void deleteObjectById(int id, TaskType type) {
        if (taskStorage.containsKey(id)) {
            switch (type) {
                case TASK:
                    Task taskForDelete = (Task) taskStorage.get(id);
                    for (Task task : tasks) {
                        if (task.equals(taskForDelete)) {
                            tasks.remove(task);
                            break;
                        }
                    }
                    break;
                case SUBTASK:
                    Subtask subtaskForDelete = (Subtask) taskStorage.get(id);
                    for (Subtask subtask : subtasks) {
                        if (subtask.equals(subtaskForDelete)) {
                            if (subtask.getEpicId() != null) {
                                Epic epic = (Epic) taskStorage.get(subtask.getEpicId());
                                subtasks.remove(subtask);
                                Epic.manageEpicStatus(epic);
                                break;
                            }
                            subtasks.remove(subtask);
                            break;
                        }
                    }
                    break;
                case EPIC:
                    Epic epicForDelete = (Epic) taskStorage.get(id);
                    for (Epic epic : epics) {
                        if (epic.equals(epicForDelete)) {
                            ArrayList<Subtask> epicSubtasks = getEpicSubtasks(epic.getTaskId());
                            if (epicSubtasks == null || epicSubtasks.isEmpty()) {
                                epics.remove(epic);
                            } else {
                                for (Subtask subtask : epicSubtasks) {
                                    deleteObjectById(subtask.getTaskId(), TaskType.SUBTASK);
                                }
                                epics.remove(epic);
                            }
                        }
                    }
                    break;
            }
            taskStorage.remove(id);
        } else {
            System.out.println("Отсутствует задание с id: " + id);
        }
    }

    public static void deleteObjects(TaskType taskType) {
        ArrayList<Integer> taskIds;

        switch (taskType) {
            case TASK:
                 taskIds = getIds(tasks);
                 tasks.clear();
                 break;
             case SUBTASK:
                 taskIds = getIds(subtasks);
                 subtasks.clear();
                 break;
             case EPIC:
                 taskIds = getIds(epics);
                 epics.clear();
                 break;
             default:
                 taskIds = new ArrayList<>();
        }
        for (Integer taskId : taskIds) {
            taskStorage.remove(taskId);
        }
    }

    public static HashMap<Integer, Object> getTaskStorage() {
        return taskStorage;
    }

    private static ArrayList<Integer> getIds(ArrayList<? extends Task> tasks) {
        ArrayList<Integer> ids = new ArrayList<>();
        for (Task task : tasks) {
            ids.add(task.taskId);
        }
        return ids;
    }
}

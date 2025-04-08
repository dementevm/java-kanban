import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");

        System.out.println("Проверки типа TASK: ");
        System.out.println("1. Создать 3 задачи.");
        Task task1 = new Task("Task1", "Task1Description");
        Task task2 = new Task("Task2", "Task2Description", 2);
        Task task3 = new Task("Task3", "Task3Description", 3, TaskStatus.IN_PROGRESS);
        System.out.println("OK");
        TaskManager.createTask(task1);
        TaskManager.createTask(task2);
        TaskManager.createTask(task3);
        System.out.println("2. Получить список задач.");
        System.out.println(TaskManager.getTasks());
        System.out.println("OK");
        System.out.println("3. Получить задачу с ID = 2");
        System.out.println(TaskManager.getObjectById(2));
        System.out.println("OK");
        System.out.println("4. Обновить задачу с ID = 3");
        Task task3Edited = new Task("Task3Edited", "Task3EditedDescription", 3, TaskStatus.IN_PROGRESS, true);
        TaskManager.updateTask(task3Edited);
        System.out.println(TaskManager.getTasks());
        System.out.println(TaskManager.getTaskStorage());
        System.out.println("OK");
        System.out.println("5. Удалить задачу с ID = 1");
        TaskManager.deleteObjectById(1, TaskType.TASK);
        System.out.println(TaskManager.getTasks());
        System.out.println(TaskManager.getTaskStorage());
        System.out.println("OK");
        System.out.println("5. Удалить все задачи");
        TaskManager.deleteObjects(TaskType.TASK);
        System.out.println(TaskManager.getTasks());
        System.out.println(TaskManager.getTaskStorage());
        System.out.println("OK");
        System.out.println("**********");
        System.out.println("Проверки типа EPIC и SUBTASK");
        System.out.println("1. Создать 2 Epic");
        Epic epic1 = new Epic("Epic1", "Epic1Description", 4);
        Epic epic2 = new Epic("Epic2", "Epic2Description", 5);
        TaskManager.createEpic(epic1);
        TaskManager.createEpic(epic2);
        System.out.println(TaskManager.getEpics());
        System.out.println(TaskManager.getTaskStorage());
        System.out.println("OK. Статусы - NEW");
        System.out.println("2. Создать 4 Subtask");
        Subtask subtask1 = new Subtask("Subtask1", "Subtask1Description", 6, 4);
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2Description", 7, 4);
        Subtask subtask3 = new Subtask("Subtask3", "Subtask3Description", 8, 4);
        Subtask subtask4 = new Subtask("Subtask4", "Subtask4Description", 9, 5, TaskStatus.IN_PROGRESS);
        TaskManager.createSubtask(subtask1);
        TaskManager.createSubtask(subtask2);
        TaskManager.createSubtask(subtask3);
        TaskManager.createSubtask(subtask4);
        System.out.println(TaskManager.getSubtasks());
        System.out.println(TaskManager.getTaskStorage());
        System.out.println("OK");
        System.out.println("3. Изменить статус у Subtask с ID - 7");
        Subtask subtask2Updated = new Subtask("Subtask2Updated", "Subtask2UpdatedDescription", 7, 4, TaskStatus.IN_PROGRESS, true);
        TaskManager.updateSubtask(subtask2Updated);
        System.out.println(TaskManager.getObjectById(7));
        System.out.println("4. Проверить статус у Epic c ID - 4");
        Epic epicWithId4 = (Epic) TaskManager.getObjectById(4);
        System.out.println(epicWithId4.getStatus());
        System.out.println("OK - Статус - IN_PROGRESS");
        System.out.println("5. Поменять статусы у SUBTASK на DONE которые относятся к EPIC с ID - 4");
        ArrayList<Subtask> epic4Subtasks = TaskManager.getEpicSubtasks(4);
        System.out.println(epic4Subtasks);
        for (Subtask subtask : epic4Subtasks) {
            subtask.setStatus(TaskStatus.DONE);
            TaskManager.updateSubtask(subtask);
        }
        System.out.println(TaskManager.getEpicSubtasks(4));
        System.out.println("OK");
        System.out.println("6. Проверить статус у EPIC с ID - 4");
        System.out.println(epicWithId4.getStatus());
        System.out.println("OK - Статус - DONE");
        System.out.println("7. Удалить SUBTASK с ID - 9, который относится к EPIC с ID - 5");
        Epic epicWithId5 = (Epic) TaskManager.getObjectById(5);
        System.out.println("Текущий статус у EPIC c ID 5 -  " + epicWithId5.getStatus());
        TaskManager.deleteObjectById(9, TaskType.SUBTASK);
        System.out.println("Текущий статус у EPIC c ID 5 -  " + epicWithId5.getStatus());
        System.out.println("Список Subtasks: " + TaskManager.getSubtasks());
        System.out.println(TaskManager.getTaskStorage());
        System.out.println("OK");
        System.out.println("8. Обновить информацию по EPIC с ID - 4");
        Epic epic4Updated = new Epic("Epic1Updated", "Epic1UpdatedDesc", 4, true);
        TaskManager.updateEpic(epic4Updated);
        System.out.println(TaskManager.getObjectById(4));
        System.out.println("OK");
        System.out.println("9. Удалить EPIC с ID - 4. Subtasks этого EPIC также удаляются.");
        TaskManager.deleteObjectById(4, TaskType.EPIC);
        System.out.println(TaskManager.getEpics());
        System.out.println(TaskManager.getTaskStorage());
        System.out.println("OK");
        System.out.println("********");
        System.out.println("Проверка автоматической генерации ID");
        System.out.println("1. Создать 2 Task без присвоение ID.");
        Task taskAutoId1 = new Task("TaskAutoId1", "TaskAutoId1Description");
        Task taskAutoId2 = new Task("TaskAutoId2", "TaskAutoId2Description");
        TaskManager.createTask(taskAutoId1);
        TaskManager.createTask(taskAutoId2);
        System.out.println(TaskManager.getTaskStorage());
        System.out.println("OK");
        System.out.println("2. Попытаться создать Task c ID - 10");
        try {
            Task taskId10 = new Task("TaskId10", "TaskId10Description", 10);
        } catch (UnsupportedOperationException e) {
            System.out.println(e.getMessage());
        }
        System.out.println("OK - Вызывается исключение.");
        System.out.println("3. Создать Task с ID - 12, который превышает внутренний ID (11)");
        Task taskId12 = new Task("TaskId12", "TaskId13Description", 12);
        TaskManager.createTask(taskId12);
        System.out.println(TaskManager.getTasks());
        System.out.println("OK");
        System.out.println("4. Создать Task без присвоения ID");
        Task taskIdAuto = new Task("TaskIdAuto", "TaskIdAutoDescription");
        TaskManager.createTask(taskIdAuto);
        System.out.println(TaskManager.getTasks());
        System.out.println("OK");
    }
}

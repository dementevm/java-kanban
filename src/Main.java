import controller.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import util.TaskStatus;

import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        System.out.println("Поехали!");
        TaskManager taskManager = new TaskManager();

        System.out.println("Проверки типа TASK: ");
        System.out.println("1. Создать 3 задачи.");
        Task task1 = new Task("Task1", "Task1Description");
        Task task2 = new Task("Task2", "Task2Description");
        Task task3 = new Task("Task3", "Task3Description", TaskStatus.IN_PROGRESS);
        System.out.println("OK");
        taskManager.createTask(task1);
        taskManager.createTask(task2);
        taskManager.createTask(task3);
        System.out.println("2. Получить список задач.");
        System.out.println(taskManager.getTasks());
        System.out.println("OK");
        System.out.println("3. Получить задачу с ID = 2");
        System.out.println(taskManager.getTask(2));
        System.out.println("OK");
        System.out.println("4. Обновить задачу с ID = 3");
        Task task3Edited = new Task("Task3Edited", "Task3EditedDescription", 3, TaskStatus.IN_PROGRESS);
        taskManager.updateTask(task3Edited);
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getTaskStorage());
        System.out.println("OK");
        System.out.println("5. Удалить задачу с ID = 1");
        taskManager.deleteTask(1);
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getTaskStorage());
        System.out.println("OK");
        System.out.println("5. Удалить все задачи");
        taskManager.deleteTasks();
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getTaskStorage());
        System.out.println("OK");
        System.out.println("**********");
        System.out.println("Проверки типа EPIC и SUBTASK");
        System.out.println("1. Создать 2 Epic");
        Epic epic1 = new Epic("Epic1", "Epic1Description");
        Epic epic2 = new Epic("Epic2", "Epic2Description");
        taskManager.createEpic(epic1);
        taskManager.createEpic(epic2);
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTaskStorage());
        System.out.println("OK. Статусы - NEW");
        System.out.println("2. Создать 4 Subtask");
        Subtask subtask1 = new Subtask("Subtask1", "Subtask1Description", 4);
        Subtask subtask2 = new Subtask("Subtask2", "Subtask2Description", 4);
        Subtask subtask3 = new Subtask("Subtask3", "Subtask3Description", 4);
        Subtask subtask4 = new Subtask("Subtask4", "Subtask4Description", 5, TaskStatus.IN_PROGRESS);
        taskManager.createSubtask(subtask1);
        taskManager.createSubtask(subtask2);
        taskManager.createSubtask(subtask3);
        taskManager.createSubtask(subtask4);
        System.out.println(taskManager.getSubtasks());
        System.out.println("OK");
        System.out.println("3. Изменить статус у Subtask с ID - 7");
        System.out.println(taskManager.getSubtasks());
        System.out.println(taskManager.getTaskStorage());
        Subtask subtask2Updated = new Subtask("Subtask2Updated", "Subtask2UpdatedDescription", 7, 4, TaskStatus.IN_PROGRESS);
        taskManager.updateSubtask(subtask2Updated);
        System.out.println(taskManager.getSubtask(7));
        System.out.println("4. Проверить статус у Epic c ID - 4");
        Epic epicWithId4 =  taskManager.getEpic(4);
        System.out.println(epicWithId4.getStatus());
        System.out.println("OK - Статус - IN_PROGRESS");
        System.out.println("5. Поменять статусы у SUBTASK на DONE которые относятся к EPIC с ID - 4");
        ArrayList<Subtask> epic4Subtasks = new ArrayList<>(epicWithId4.getSubtasks());
        for (Subtask subtask : epic4Subtasks) {
            subtask.setStatus(TaskStatus.DONE);
            taskManager.updateSubtask(subtask);
        }
        System.out.println(epicWithId4.getStatus());
        System.out.println("OK");
        System.out.println("6. Проверить статус у EPIC с ID - 4");
        System.out.println(epicWithId4.getStatus());
        System.out.println("OK - Статус - DONE");
        System.out.println("7. Удалить SUBTASK с ID - 9, который относится к EPIC с ID - 5");
        Epic epicWithId5 = taskManager.getEpic(5);
        System.out.println("Текущий статус у EPIC c ID 5 -  " + epicWithId5.getStatus());
        taskManager.deleteSubtask(9);
        System.out.println("Новый статус у EPIC c ID 5 -  " + epicWithId5.getStatus());
        System.out.println("Список Subtasks: " + taskManager.getSubtasks());
        System.out.println(taskManager.getTaskStorage());
        System.out.println("OK");
        System.out.println("8. Обновить информацию по EPIC с ID - 4");
        Epic epic4Updated = new Epic("Epic1Updated", "Epic1UpdatedDesc", 4);
        taskManager.updateEpic(epic4Updated);
        System.out.println(taskManager.getEpic(4));
        System.out.println("OK");
        System.out.println("9. Удалить EPIC с ID - 4. Subtasks этого EPIC также удаляются.");
        taskManager.deleteEpic(4);
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTaskStorage());
        System.out.println("OK");
    }
}

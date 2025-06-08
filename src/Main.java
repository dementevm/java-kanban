import controller.FileBackedTaskManager;
import controller.TaskManager;
import exceptions.TaskNotFound;

import java.io.IOException;

public class Main {

    public static void main(String[] args) throws IOException, TaskNotFound {
        TaskManager taskManager = new FileBackedTaskManager();
    }
}
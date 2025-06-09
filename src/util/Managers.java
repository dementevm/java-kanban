package util;

import controller.HistoryManager;
import controller.InMemoryHistoryManager;
import controller.InMemoryTaskManager;
import controller.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}

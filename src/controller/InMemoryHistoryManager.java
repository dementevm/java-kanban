package controller;

import model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    private final LinkedList<Task> inMemoryHistoryList = new LinkedList<>();

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryList;
    }

    @Override
    public void add(Task task) {
        inMemoryHistoryList.add(task);
        if (inMemoryHistoryList.size() > 10) {
            inMemoryHistoryList.removeFirst();
        }
    }
}

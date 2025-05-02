package controller;

import model.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {
    LinkedList<Task> inMemoryHistoryList = new LinkedList<>();

    @Override
    public List<Task> getHistory() {
        return inMemoryHistoryList;
    }

    @Override
    public void add(Task task) {
        if (inMemoryHistoryList.size() == 10) {
            inMemoryHistoryList.removeFirst();
            inMemoryHistoryList.add(task);
        } else {
            inMemoryHistoryList.add(task);
        }
    }
}

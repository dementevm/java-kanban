package controller;

import model.Task;
import model.Node;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node<Task>> inMemoryHistoryMap = new HashMap<>();
    private Node<Task> head;
    private Node<Task> tail;

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        Node<Task> nodeForRemove = inMemoryHistoryMap.get(id);
        if (nodeForRemove != null) {
            removeNode(nodeForRemove);
        }
        inMemoryHistoryMap.remove(id);
    }

    @Override
    public void add(Task task) {
        if (inMemoryHistoryMap.containsKey(task.getTaskId())) {
            removeNode(inMemoryHistoryMap.get(task.getTaskId()));
            inMemoryHistoryMap.remove(task.getTaskId());
        }
        inMemoryHistoryMap.put(task.getTaskId(), linkLast(task));
    }

    public Node<Task> linkLast(Task task) {
        Node<Task> newNode;
        if (head == null && tail == null) {
            newNode = new Node<>(null, task, null);
            head = newNode;
        } else if (tail == null) {
            newNode = new Node<>(head, task, null);
            head.next = newNode;
            tail = newNode;
        } else {
            Node<Task> oldTail = tail;
            newNode = new Node<>(oldTail, task, null);
            oldTail.next = newNode;
            tail = newNode;
        }
        return newNode;
    }

    public List<Task> getTasks() {
        List<Task> historyList = new ArrayList<>();
        Node<Task> current = head;
        if (current != null) {
            while (current != null) {
                historyList.add(current.data);
                current = current.next;
            }
        }
        return historyList;
    }

    public void removeNode(Node<Task> node) {
        Node<Task> prev = node.prev;
        if (node.equals(head)) {
            head = node.next;
            if (head != null) {
                head.prev = null;
            }
            return;
        }

        if (node.equals(tail)) {
            tail = node.prev;
            tail.next = null;
            return;
        }

        Node<Task> next = node.next;
        prev.next = next;
        next.prev = prev;
    }
}
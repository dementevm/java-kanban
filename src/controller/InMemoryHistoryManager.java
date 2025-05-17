package controller;

import model.Task;

import java.util.*;

public class InMemoryHistoryManager implements HistoryManager {
    private final Map<Integer, Node> inMemoryHistoryMap = new HashMap<>();
    private Node head;
    private Node tail;

    public static class Node {
        public Node prev;
        public Task data;
        public Node next;

        public Node(Node prev, Task data, Node next) {
            this.prev = prev;
            this.data = data;
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (this == o) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(prev, node.prev) && Objects.equals(data, node.data) && Objects.equals(next, node.next);
        }

        @Override
        public int hashCode() {
            return Objects.hashCode(data);
        }
    }

    @Override
    public List<Task> getHistory() {
        return getTasks();
    }

    @Override
    public void remove(int id) {
        Node nodeForRemove = inMemoryHistoryMap.get(id);
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

    private Node linkLast(Task task) {
        final Node newNode = new Node(tail, task, null);
        if (head == null) {
            head = newNode;
        } else {
            tail.next = newNode;
        }
        tail = newNode;
        return tail;
    }

    private List<Task> getTasks() {
        List<Task> historyList = new ArrayList<>();
        Node current = head;
        if (current != null) {
            while (current != null) {
                historyList.add(current.data);
                current = current.next;
            }
        }
        return historyList;
    }

    private void removeNode(Node node) {
        Node prev = node.prev;
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

        Node next = node.next;
        prev.next = next;
        next.prev = prev;
    }
}
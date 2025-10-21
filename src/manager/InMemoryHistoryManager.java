package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.Objects;

public class InMemoryHistoryManager implements HistoryManager {
    Node head = null;
    Node tail = null;
    Map<Integer, Node> nodes;

    public InMemoryHistoryManager() {
        nodes = new HashMap<>();
    }

    @Override
    public void add(Task task) {
        if (task == null) return;
        if (nodes.containsKey(task.getId())) removeNode(nodes.get(task.getId()));
        Node node = new Node(task, tail, null);
        linkLast(node);
    }

    @Override
    public void remove(int id) {
        removeNode(nodes.get(id));
        nodes.remove(id);
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }

    private Node linkLast(Node node) {
        if (head == null) head = node;
        else tail.setNext(node);
        tail = node;
        nodes.put(node.getValue().getId(), node);
        return node;
    }

    private Node removeNode(Node node) {
        if (node == null) return null;
        if (node.getPrev() != null) node.getPrev().setNext(node.getNext());
        if (node.getNext() != null) node.getNext().setPrev(node.getPrev());
        if (node == head) head = node.getNext();
        if (node == tail) tail = node.getPrev();
        return node;
    }

    private ArrayList<Task> getTasks() {
        Node node = head;
        ArrayList<Task> history = new ArrayList<>();
        while (node != null) {
            history.add(node.getValue());
            node = node.getNext();
        }
        return history;
    }

    private static class Node {
        Task value;
        Node prev;
        Node next;

        public Node(Task value, Node prev, Node next) {
            this.value = value;
            this.prev = prev;
            this.next = next;
        }

        public Task getValue() {
            return value;
        }

        public void setValue(Task value) {
            this.value = value;
        }

        public Node getPrev() {
            return prev;
        }

        public void setPrev(Node prev) {
            this.prev = prev;
        }

        public Node getNext() {
            return next;
        }

        public void setNext(Node next) {
            this.next = next;
        }

        @Override
        public boolean equals(Object o) {
            if (o == this) return true;
            if (o == null || getClass() != o.getClass()) return false;
            Node node = (Node) o;
            return Objects.equals(value, node.value) && Objects.equals(prev, node.prev) && Objects.equals(next, node.next);
        }

        @Override
        public int hashCode() {
            return Objects.hash(value, prev, next);
        }

        @Override
        public String toString() {
            return "Node{" +
                    "value.getId=" + value.getId() +
                    ", prev.getValue.getId=" + prev.getValue().getId() +
                    ", next.getValue.getId=" + next.getValue().getId() +
                    '}';
        }
    }
}

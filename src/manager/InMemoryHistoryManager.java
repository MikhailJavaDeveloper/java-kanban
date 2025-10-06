package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    Node head = null;
    Node tail = null;
    Map<Integer, Node> nodes;

    public InMemoryHistoryManager() {
        nodes = new HashMap<>();
    }

    @Override
    public Node add(Task task) {
        if (task == null) return null;
        if (nodes.containsKey(task.getId())) removeNode(nodes.get(task.getId()));
        Node node = new Node(task, tail, null);
        return linkLast(node);
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
}

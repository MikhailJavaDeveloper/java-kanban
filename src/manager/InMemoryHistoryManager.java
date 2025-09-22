package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryHistoryManager implements HistoryManager {
    Node head = null;
    Node tail = null;
    Map<Integer, Node> taskNodesById;

    public InMemoryHistoryManager() {
        taskNodesById = new HashMap<>();
    }

    @Override
    public Node add(Task task) {
        if (task == null) return null;
        if (taskNodesById.containsKey(task.getId())) removeNode(taskNodesById.get(task.getId()));
        Node node = new Node(task, tail, null);
        return linkLast(node);
    }

    @Override
    public Node linkLast(Node node) {
        if (node == null) return null;
        if (taskNodesById.containsKey(node.getValue().getId())) removeNode(node);
        tail = node;
        if (head == null) head = node;
        else node.getPrev().setNext(node);
        taskNodesById.put(node.getValue().getId(), node);
        return node;
    }

    @Override
    public Node removeNode(Node node) {
        if (node == null) return null;
        if (node.getPrev() != null) node.getPrev().setNext(node.getNext());
        if (node.getNext() != null) node.getNext().setPrev(node.getPrev());
        if (node == head) head = node.getNext();
        if (node == tail) tail = node.getPrev();
        taskNodesById.remove(node.getValue().getId());
        return node;
    }

    @Override
    public void remove(int id) {
        removeNode(taskNodesById.get(id));
    }

    @Override
    public ArrayList<Task> getTasks() {
        Node node = head;
        ArrayList<Task> history = new ArrayList<>();
        while (node != null) {
            history.add(node.getValue());
            node = node.getNext();
        }
        return history;
    }

    @Override
    public ArrayList<Task> getHistory() {
        return getTasks();
    }
}

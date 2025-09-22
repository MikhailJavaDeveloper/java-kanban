package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;

public class InMemoryHistoryManager implements HistoryManager {
    Node head = null;
    Node tail = null;
    HashMap<Integer, Node> taskNodesById;

    public InMemoryHistoryManager() {
        taskNodesById = new HashMap<>();
    }

    @Override
    public Node add(Task task) {
        if (task == null) return null;
        if (taskNodesById.containsKey(task.getId())) removeNode(taskNodesById.get(task.getId()));
        Node node = new Node(task, tail, null);
        tail = node;
        if (head == null) head = node;
        else node.getPrev().setNext(node);
        taskNodesById.put(task.getId(), node);
        return node;
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
        if (node.getPrev() != null) node.getPrev().setNext(node.getNext());
        if (node.getNext() != null) node.getNext().setPrev(node.getPrev());
        return node;
    }

    @Override
    public ArrayList<Task> getTasks() {
        Node node = head;
        ArrayList<Task> history = new ArrayList<>();
        while(node != null) {
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

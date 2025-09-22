package manager;

import tasks.*;

import java.util.ArrayList;

public interface HistoryManager {
    public Node add(Task task);

    public Node linkLast(Node node);

    public Node removeNode(Node node);

    public void remove(int id);

    public ArrayList<Task> getTasks();

    public ArrayList<Task> getHistory();
}

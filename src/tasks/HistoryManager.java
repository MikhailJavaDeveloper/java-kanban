package tasks;

public interface HistoryManager {
    public void add(Task task);

    public Task[] getHistory();
}

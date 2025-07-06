package tasks;

public class InMemoryHistoryManager implements HistoryManager {
    private final Task[] history;
    private int sizeOfHistory;

    public InMemoryHistoryManager() {
        history = new Task[10];
        sizeOfHistory = 0;
    }

    public void add(Task task) {
        if (sizeOfHistory == 10) {
            for (int i = 0; i < 9; i++) {
                history[i] = history[i+1];
            }
            history[9] = task;
        } else {
            history[sizeOfHistory] = task;
            sizeOfHistory++;
        }
    }

    @Override
    public Task[] getHistory() {
        return history;
    }
}

package tasks;

public class Subtask extends Task {
    private final Epic epic;

    public Subtask(String name, String description, Statuses status, Epic epic) {
        super(name, description, status);
        this.epic = epic;
        epic.addSubtask(this);
    }

    public Epic getEpic() {
        return epic;
    }

    @Override
    public String toString() {
        return "tasks.Subtask{" +
                "name='" + getName() + '\'' +
                ", description.length=" + getDescription().length() +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", epic.getName=" + epic.getName() +
                '}';
    }
}

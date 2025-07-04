package tasks;

public class Subtask extends Task {
    private Epic epic;

    public Subtask(String name, String description, Statuses status, Epic epic) {
        super(name, description, status);
        this.epic = epic;
        epic.addSubtask(this);
    }

    public Subtask(Subtask oldSubtask, String name, String description, Statuses status) {
        super(name, description, status);
        this.setId(oldSubtask.getId());
        this.epic = oldSubtask.getEpic();
        epic.removeSubtask(oldSubtask);
        epic.addSubtask(this);
    }

    public Epic getEpic() {
        return epic;
    }

    void setEpic(Epic epic) {
        this.epic = epic;
    }

    @Override
    public String toString() {
        return "Subtask{" +
                "name='" + getName() + '\'' +
                ", description.length=" + getDescription().length() +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", epic.getName=" + epic.getName() +
                '}';
    }
}

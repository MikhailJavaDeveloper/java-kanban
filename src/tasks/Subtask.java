package tasks;

public class Subtask extends Task {
    private Epic epic;
    private int epicId;

    public Subtask(String name, String description, TaskStatuses status, Epic epic) {
        super(name, description, status);
        this.epic = epic;
        epic.addSubtask(this);
        type = TaskTypes.SUBTASK;
    }

    public Subtask(String name, String description, TaskStatuses status, int epicId) {
        super(name, description, status);
        this.epicId = epicId;
        type = TaskTypes.SUBTASK;
    }

    public Subtask(Subtask oldSubtask, String name, String description, TaskStatuses status) {
        super(name, description, status);
        this.setId(oldSubtask.getId());
        this.epic = oldSubtask.getEpic();
        epic.removeSubtask(oldSubtask);
        epic.addSubtask(this);
        type = TaskTypes.SUBTASK;
    }

    public Epic getEpic() {
        return epic;
    }

    public void setEpic(Epic epic) {
        this.epic = epic;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Subtask subtask = (Subtask) o;
        return getId() == subtask.getId();
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

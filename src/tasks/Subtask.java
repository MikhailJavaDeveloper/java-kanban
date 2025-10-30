package tasks;

import java.time.Duration;
import java.time.LocalDateTime;

public class Subtask extends Task {
    private Epic epic;
    private int epicId;

    public Subtask(String name, String description, TaskStatuses status, Epic epic, Duration duration,
            LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.epic = epic;
        epic.addSubtask(this);
        type = TaskTypes.SUBTASK;
    }

    public Subtask(String name, String description, TaskStatuses status, int epicId, Duration duration,
            LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
        this.epicId = epicId;
        type = TaskTypes.SUBTASK;
    }

    public Subtask(Subtask oldSubtask, String name, String description, TaskStatuses status, Duration duration,
                   LocalDateTime startTime) {
        super(name, description, status, duration, startTime);
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
                ", duration=" + getDurationInMinutes() +
                ", startTime=" + getStartTime() +
                ", epic.getName=" + epic.getName() +
                '}';
    }
}

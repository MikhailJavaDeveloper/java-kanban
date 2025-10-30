package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private TaskStatuses status;
    private int id;
    protected TaskTypes type = TaskTypes.TASK;
    private final Duration duration;
    private final LocalDateTime startTime;

    public Task(String name, String description, TaskStatuses status, Duration duration, LocalDateTime startTime) {
        this.name = name;
        this.description = description;
        this.status = status;
        this.duration = duration;
        this.startTime = startTime;
    }

    public Task(Task oldTask, String name, String description, TaskStatuses status, Duration duration,
            LocalDateTime startTime) {
        this(name, description, status, duration, startTime);
        this.id = oldTask.id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public TaskStatuses getStatus() {
        return status;
    }

    protected void setStatus(TaskStatuses status) {
        this.status = status;
    }

    public TaskTypes getType() {
        return type;
    }

    public long getDurationInMinutes() {
        return duration.toMinutes();
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return startTime.plus(duration);
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Task task = (Task) o;
        return id == task.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }

    @Override
    public String toString() {
        return "Task{" +
                "name='" + name + '\'' +
                ", description.length=" + description.length() +
                ", id=" + id +
                ", status=" + status +
                ", duration=" + getDurationInMinutes() +
                ", startTime=" + getStartTime() +
                '}';
    }
}

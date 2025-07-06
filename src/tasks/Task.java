package tasks;

import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private TaskStatuses status;
    private int id;

    public Task(String name, String description, TaskStatuses status) {
        this.name = name;
        this.description = description;
        this.status = status;
    }

    public Task(Task oldTask, String name, String description, TaskStatuses status) {
        this(name, description, status);
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

    protected void setId(int id) {
        this.id = id;
    }

    public TaskStatuses getStatus() {
        return status;
    }

    protected void setStatus(TaskStatuses status) {
        this.status = status;
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
                '}';
    }
}

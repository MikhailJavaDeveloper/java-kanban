package tasks;

import java.util.Objects;

public class Task {
    private String name;
    private String description;
    private int id;
    private Statuses status;

    public Task(String name, String description, Statuses status) {
        this.name = name;
        this.description = description;
        this.status = status;
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

    public Statuses getStatus() {
        return status;
    }

    protected void setStatus(Statuses status) {
        this.status = status;
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

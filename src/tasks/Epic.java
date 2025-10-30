package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description, TaskStatuses.NEW, Duration.ofMinutes(0),
            LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0));
        this.subtasks = new ArrayList<>();
        type = TaskTypes.EPIC;
    }

    public Epic(Epic oldEpic, String name, String description) {
        super(name, description, TaskStatuses.NEW, Duration.ofMinutes(0),
                LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0));
        this.setId(oldEpic.getId());
        this.subtasks = oldEpic.getSubtasks();
        subtasks.forEach(s -> s.setEpic(this));
        checkStatus();
        type = TaskTypes.EPIC;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        checkStatus();
    }

    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
        checkStatus();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
        checkStatus();
    }

    @Override
    public boolean equals(Object o) {
        if (o == this) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Epic epic = (Epic) o;
        return getId() == epic.getId();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "name='" + getName() + '\'' +
                ", description.length=" + getDescription().length() +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", duration=" + getDurationInMinutes() +
                ", startTime=" + getStartTime() +
                ", subtasks.size=" + subtasks.size() +
                '}';
    }

    private void checkStatus() {
        if (subtasks.isEmpty()) {
            this.setStatus(TaskStatuses.NEW);
            return;
        }

        long statusNewCount = subtasks.stream()
                .filter(s -> s.getStatus() == TaskStatuses.NEW)
                .count();
        long statusDoneCount = subtasks.stream()
                .filter(s -> s.getStatus() == TaskStatuses.DONE)
                .count();
        if (subtasks.size() == statusNewCount) this.setStatus(TaskStatuses.NEW);
        else if (subtasks.size() == statusDoneCount) this.setStatus(TaskStatuses.DONE);
        else this.setStatus(TaskStatuses.IN_PROGRESS);
    }
}

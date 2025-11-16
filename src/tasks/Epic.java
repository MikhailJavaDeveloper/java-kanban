package tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.Optional;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;
    private LocalDateTime endTime;

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
        checkStatusDurationAndStartTime();
        type = TaskTypes.EPIC;
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        checkStatusDurationAndStartTime();
    }

    public void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
        checkStatusDurationAndStartTime();
    }

    public ArrayList<Subtask> getSubtasks() {
        return subtasks;
    }

    public void setSubtasks(ArrayList<Subtask> subtasks) {
        this.subtasks = subtasks;
        checkStatusDurationAndStartTime();
    }

    @Override
    public LocalDateTime getEndTime() {
        endTime = getSubtasks().stream()
                .map(Subtask::getEndTime)
                .max(LocalDateTime::compareTo)
                .get();
        return endTime;
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
                ", endTime=" + getEndTime() +
                ", subtasks.size=" + subtasks.size() +
                '}';
    }

    private void checkStatusDurationAndStartTime() {
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

        long duration = getSubtasks().stream()
                .mapToLong(Subtask::getDurationInMinutes)
                .sum();
        setDuration(duration);

        Optional<LocalDateTime> earliest = getSubtasks().stream()
                .map(Subtask::getStartTime)
                .min(LocalDateTime::compareTo);
        setStartTime(earliest.get());
    }
}

package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description, TaskStatuses.NEW);
        this.subtasks = new ArrayList<>();
    }

    public Epic(Epic oldEpic, String name, String description) {
        super(name, description, TaskStatuses.NEW);
        this.setId(oldEpic.getId());
        this.subtasks = oldEpic.getSubtasks();
        for (Subtask subtask : subtasks) {
            subtask.setEpic(this);
        }
        checkStatus();
    }

    public void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        checkStatus();
    }

    void removeSubtask(Subtask subtask) {
        subtasks.remove(subtask);
        checkStatus();
    }

    ArrayList<Subtask> getSubtasks() {
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
                ", subtasks.size=" + subtasks.size() +
                '}';
    }

    private void checkStatus() {
        if (subtasks.isEmpty()) {
            this.setStatus(TaskStatuses.NEW);
            return;
        }

        int statusNewCount = 0;
        int statusDoneCount = 0;
        for (Subtask subtask1 : subtasks) {
            if (subtask1.getStatus().equals(TaskStatuses.NEW)) statusNewCount++;
            else if(subtask1.getStatus().equals(TaskStatuses.DONE)) statusDoneCount++;
        }
        if (subtasks.size() == statusNewCount) this.setStatus(TaskStatuses.NEW);
        else if (subtasks.size() == statusDoneCount) this.setStatus(TaskStatuses.DONE);
        else this.setStatus(TaskStatuses.IN_PROGRESS);
    }
}

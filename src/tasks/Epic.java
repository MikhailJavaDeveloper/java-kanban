package tasks;

import java.util.ArrayList;

public class Epic extends Task {
    private final ArrayList<Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description, Statuses.NEW);
        this.subtasks = new ArrayList<>();
    }

    void addSubtask(Subtask subtask) {
        subtasks.add(subtask);
        checkStatus();
    }

    void deleteSubtask(Subtask subtask) {
        subtasks.remove(subtask);
        checkStatus();
    }

    void checkStatus() {
        if (subtasks.isEmpty()) {
            this.setStatus(Statuses.NEW);
            return;
        }

        int statusNewCount = 0;
        int statusDoneCount = 0;
        for (Subtask subtask1 : subtasks) {
            if (subtask1.getStatus().equals(Statuses.NEW)) statusNewCount++;
            else if(subtask1.getStatus().equals(Statuses.DONE)) statusDoneCount++;
        }
        if (subtasks.size() == statusNewCount) this.setStatus(Statuses.NEW);
        else if (subtasks.size() == statusDoneCount) this.setStatus(Statuses.DONE);
        else this.setStatus(Statuses.IN_PROGRESS);
    }

    void printSubtasks() {
        for (Subtask subtask : subtasks) {
            System.out.println(subtask.getName());
        }
    }

    @Override
    public String toString() {
        return "tasks.Epic{" +
                "name='" + getName() + '\'' +
                ", description.length=" + getDescription().length() +
                ", id=" + getId() +
                ", status=" + getStatus() +
                ", subtasks.size()=" + subtasks.size() +
                '}';
    }
}

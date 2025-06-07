package tasks;

import java.util.HashMap;

public class TasksManager {
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Subtask> subtasks;
    private final HashMap<Integer, Epic> epics;

    public TasksManager() {
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
    }


    public void printTasks() {
        for (Task task : tasks.values()) {
            System.out.println(task);
        }
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void putTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void renewTask(Task task) {
        tasks.put(task.getId(), task);
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }


    public void printSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            System.out.println(subtask);
        }
    }

    public void deleteSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            subtask.getEpic().deleteSubtask(subtask);
        }
        subtasks.clear();
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void putSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    public void renewSubtask(Subtask subtask) {
        subtasks.put(subtask.getId(), subtask);
    }

    public void deleteSubtaskById(int id) {
        subtasks.get(id).getEpic().deleteSubtask(subtasks.get(id));
        subtasks.remove(id);
    }


    public void printEpics() {
        for (Epic epic : epics.values()) {
            System.out.println(epic);
        }
    }

    public void deleteEpics() {
        epics.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void putEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void renewEpic(Epic epic) {
        epics.put(epic.getId(), epic);
    }

    public void deleteEpicById(int id) {
        epics.remove(id);
    }


    public void printEpicSubtasks(Epic epic) {
        epic.printSubtasks();
    }
}
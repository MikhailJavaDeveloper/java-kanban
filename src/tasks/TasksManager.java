package tasks;

import java.util.ArrayList;
import java.util.HashMap;

public class TasksManager {
    private int taskId;
    private final HashMap<Integer, Task> tasks;
    private final HashMap<Integer, Subtask> subtasks;
    private final HashMap<Integer, Epic> epics;

    public TasksManager() {
        taskId = 0;
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
    }


    public HashMap<Integer, Task> getTasks() {
        return tasks;
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void putTask(Task task) {
        tasks.put(generateTaskId(), task);
    }

    public void renewTask(Task task) {
        tasks.put(generateTaskId(), task);
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }


    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
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
        subtasks.put(generateTaskId(), subtask);
    }

    public void renewSubtask(Subtask subtask) {
        subtasks.put(generateTaskId(), subtask);
    }

    public void deleteSubtaskById(int id) {
        subtasks.get(id).getEpic().deleteSubtask(subtasks.get(id));
        subtasks.remove(id);
    }


    public HashMap<Integer, Epic> getEpics() {
        return epics;
    }

    public void deleteEpics() {
        epics.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void putEpic(Epic epic) {
        epics.put(generateTaskId(), epic);
    }

    public void renewEpic(Epic epic) {
        epics.put(generateTaskId(), epic);
    }

    public void deleteEpicById(int id) {
        for (Subtask subtask : epics.get(id).getSubtasks()) {
            epics.get(id).deleteSubtask(subtask);
        }
        epics.remove(id);
    }


    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        return epic.getSubtasks();
    }

    private int generateTaskId() {
        return ++taskId;
    }
}
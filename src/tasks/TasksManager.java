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


    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();
        for (Task task : tasks.values()) {
            tasksList.add(task);
        }
        return tasksList;
    }

    public void deleteTasks() {
        tasks.clear();
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public void putTask(Task task) {
        task.setId(generateTaskId());
        tasks.put(task.getId(), task);
    }

    public void renewTask(Task oldTask, Task newTask) {
        newTask.setId(oldTask.getId());
        tasks.put(newTask.getId(), newTask);
    }

    public void deleteTaskById(int id) {
        tasks.remove(id);
    }


    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            subtasksList.add(subtask);
        }
        return subtasksList;
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
        subtask.setId(generateTaskId());
        subtasks.put(subtask.getId(), subtask);
    }

    public void renewSubtask(Subtask oldSubtask, Subtask newSubtask) {
        newSubtask.setId(oldSubtask.getId());
        newSubtask.getEpic().deleteSubtask(oldSubtask);
        subtasks.put(newSubtask.getId(), newSubtask);
    }

    public void deleteSubtaskById(int id) {
        subtasks.get(id).getEpic().deleteSubtask(subtasks.get(id));
        subtasks.remove(id);
    }


    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicsList = new ArrayList<>();
        for (Epic epic : epics.values()) {
            epicsList.add(epic);
        }
        return epicsList;
    }

    public void deleteEpics() {
        subtasks.clear();
        epics.clear();
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void putEpic(Epic epic) {
        epic.setId(generateTaskId());
        epics.put(epic.getId(), epic);
    }

    public void renewEpic(Epic oldEpic, Epic newEpic) {
        newEpic.setId(oldEpic.getId());
        newEpic.setSubtasks(oldEpic.getSubtasks());
        for (Subtask subtask : newEpic.getSubtasks()) {
            subtask.setEpic(newEpic);
        }
        epics.put(newEpic.getId(), newEpic);
    }

    public void deleteEpicById(int id) {
        for (Subtask subtask : epics.get(id).getSubtasks()) {
            subtasks.remove(subtask.getId());
        }
        epics.remove(id);
    }


    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        return epic.getSubtasks();
    }

    private int generateTaskId() {
        return taskId++;
    }
}
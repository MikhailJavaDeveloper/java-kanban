package manager;

import tasks.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class InMemoryTaskManager implements TaskManager {
    protected int taskId;
    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Subtask> subtasks;
    protected final Map<Integer, Epic> epics;
    protected final HistoryManager historyManager;

    public InMemoryTaskManager() {
        taskId = 1;
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
    }

    @Override
    public ArrayList<Task> getTasks() {
        ArrayList<Task> tasksList = new ArrayList<>();
        for (Task task : tasks.values()) {
            tasksList.add(task);
        }
        return tasksList;
    }

    @Override
    public boolean clearTasks() {
        for (Task task: tasks.values()) {
            historyManager.remove(task.getId());
        }
        tasks.clear();
        return tasks.isEmpty();
    }

    @Override
    public Task getTaskById(int id) {
        historyManager.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Task putTask(Task task) {
        task.setId(generateTaskId());
        tasks.put(task.getId(), task);
        return tasks.get(task.getId());
    }

    @Override
    public Task renewTask(Task newTask) {
        tasks.put(newTask.getId(), newTask);
        return tasks.get(newTask.getId());
    }

    @Override
    public Task removeTaskById(int id) {
        historyManager.remove(id);
        return tasks.remove(id);
    }


    @Override
    public ArrayList<Subtask> getSubtasks() {
        ArrayList<Subtask> subtasksList = new ArrayList<>();
        for (Subtask subtask : subtasks.values()) {
            subtasksList.add(subtask);
        }
        return subtasksList;
    }

    @Override
    public boolean clearSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
            subtask.getEpic().removeSubtask(subtask);
        }
        subtasks.clear();
        return subtasks.isEmpty();
    }

    @Override
    public Subtask getSubtaskById(int id) {
        historyManager.add(subtasks.get(id));
        return subtasks.get(id);
    }

    @Override
    public Subtask putSubtask(Subtask subtask) {
        subtask.setId(generateTaskId());
        subtasks.put(subtask.getId(), subtask);
        return subtasks.get(subtask.getId());
    }

    @Override
    public Subtask renewSubtask(Subtask newSubtask) {
        subtasks.put(newSubtask.getId(), newSubtask);
        return subtasks.get(newSubtask.getId());
    }

    @Override
    public Subtask removeSubtaskById(int id) {
        historyManager.remove(id);
        subtasks.get(id).getEpic().removeSubtask(subtasks.get(id));
        return subtasks.remove(id);
    }


    @Override
    public ArrayList<Epic> getEpics() {
        ArrayList<Epic> epicsList = new ArrayList<>();
        for (Epic epic : epics.values()) {
            epicsList.add(epic);
        }
        return epicsList;
    }

    @Override
    public boolean clearEpics() {
        for (Epic epic: epics.values()) {
            historyManager.remove(epic.getId());
        }
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
        }
        subtasks.clear();
        epics.clear();
        return epics.isEmpty();
    }

    @Override
    public Epic getEpicById(int id) {
        historyManager.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Epic putEpic(Epic epic) {
        epic.setId(generateTaskId());
        epics.put(epic.getId(), epic);
        return epics.get(epic.getId());
    }

    @Override
    public Epic renewEpic(Epic newEpic) {
        epics.put(newEpic.getId(), newEpic);
        return epics.get(newEpic.getId());
    }

    @Override
    public Epic removeEpicById(int id) {
        historyManager.remove(id);
        for (Subtask subtask : epics.get(id).getSubtasks()) {
            historyManager.remove(subtask.getId());
            subtasks.remove(subtask.getId());
        }
        return epics.remove(id);
    }


    @Override
    public ArrayList<Subtask> getEpicSubtasks(Epic epic) {
        return epic.getSubtasks();
    }

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    protected int generateTaskId() {
        return taskId++;
    }
}
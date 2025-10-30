package manager;

import tasks.*;

import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {
    protected int taskId;
    protected final Map<Integer, Task> tasks;
    protected final Map<Integer, Subtask> subtasks;
    protected final Map<Integer, Epic> epics;
    protected final HistoryManager historyManager;
    protected final Set<Task> prioritizedTasks;

    public InMemoryTaskManager() {
        taskId = 1;
        this.tasks = new HashMap<>();
        this.subtasks = new HashMap<>();
        this.epics = new HashMap<>();
        historyManager = Managers.getDefaultHistory();
        prioritizedTasks = new TreeSet<>(Comparator.comparing(Task::getStartTime));
    }

    @Override
    public List<Task> getTasks() {
        return tasks.values().stream()
                .toList();
    }

    @Override
    public boolean clearTasks() {
        for (Task task : tasks.values()) {
            historyManager.remove(task.getId());
            prioritizedTasks.remove(task);
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
        addTaskInPT(task);
        return tasks.get(task.getId());
    }

    @Override
    public Task renewTask(Task newTask) {
        tasks.put(newTask.getId(), newTask);
        prioritizedTasks.remove(newTask);
        addTaskInPT(newTask);
        return tasks.get(newTask.getId());
    }

    @Override
    public Task removeTaskById(int id) {
        historyManager.remove(id);
        prioritizedTasks.remove(tasks.get(id));
        return tasks.remove(id);
    }


    @Override
    public List<Subtask> getSubtasks() {
        return subtasks.values().stream()
                .toList();
    }

    @Override
    public boolean clearSubtasks() {
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
            subtask.getEpic().removeSubtask(subtask);
            prioritizedTasks.remove(subtask);
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
        addTaskInPT(subtask);
        return subtasks.get(subtask.getId());
    }

    @Override
    public Subtask renewSubtask(Subtask newSubtask) {
        subtasks.put(newSubtask.getId(), newSubtask);
        prioritizedTasks.remove(newSubtask);
        addTaskInPT(newSubtask);
        return subtasks.get(newSubtask.getId());
    }

    @Override
    public Subtask removeSubtaskById(int id) {
        historyManager.remove(id);
        subtasks.get(id).getEpic().removeSubtask(subtasks.get(id));
        prioritizedTasks.remove(subtasks.get(id));
        return subtasks.remove(id);
    }


    @Override
    public List<Epic> getEpics() {
        return epics.values().stream()
                .toList();
    }

    @Override
    public boolean clearEpics() {
        for (Epic epic: epics.values()) {
            historyManager.remove(epic.getId());
        }
        for (Subtask subtask : subtasks.values()) {
            historyManager.remove(subtask.getId());
            prioritizedTasks.remove(subtask);
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
            prioritizedTasks.remove(subtasks.get(subtask.getId()));
            subtasks.remove(subtask.getId());
        }
        return epics.remove(id);
    }


    @Override
    public List<Subtask> getEpicSubtasks(Epic epic) {
        return epic.getSubtasks();
    }

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return prioritizedTasks.stream().toList();
    }

    protected int generateTaskId() {
        return taskId++;
    }

    private void addTaskInPT(Task task) {
        if (task.getStartTime() != null && !hasOverlap(task)) {
            prioritizedTasks.add(task);
        }
    }

    private boolean isOverlap(Task task1, Task task2) {
        LocalDateTime start1 = task1.getStartTime();
        LocalDateTime end1 = task1.getEndTime();
        LocalDateTime start2 = task2.getStartTime();
        LocalDateTime end2 = task2.getEndTime();

        return start1.isBefore(end2) && start2.isBefore(end1);
    }

    public boolean hasOverlap(Task task) {
        return prioritizedTasks.stream()
                .anyMatch(t -> isOverlap(task, t));
    }
}
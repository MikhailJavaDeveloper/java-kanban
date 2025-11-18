package manager;

import exceptions.HasOverlapsException;
import exceptions.NotFoundException;
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
    public Optional<Task> getTaskById(int id) throws NotFoundException {
        historyManager.add(tasks.get(id));
        Optional<Task> taskOpt = Optional.ofNullable(tasks.get(id));
        if (taskOpt.isPresent()) {
            return taskOpt;
        } else {
            throw new NotFoundException("Задача не найдена.");
        }
    }

    @Override
    public void putTask(Task task) throws HasOverlapsException {
        if (!hasOverlap(task)) {
            task.setId(generateTaskId());
            tasks.put(task.getId(), task);
            addTaskInPT(task);
        } else {
            throw new HasOverlapsException("Перекрывает другие задачи.");
        }
    }

    @Override
    public void renewTask(Task newTask) throws HasOverlapsException {
        tasks.remove(newTask.getId());
        prioritizedTasks.remove(newTask);
        if (!hasOverlap(newTask)) {
            tasks.put(newTask.getId(), newTask);
            addTaskInPT(newTask);
        } else {
            throw new HasOverlapsException("Перекрывает другие задачи.");
        }
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
    public Optional<Subtask> getSubtaskById(int id) throws NotFoundException {
        historyManager.add(subtasks.get(id));
        Optional<Subtask> subtaskOpt = Optional.ofNullable(subtasks.get(id));
        if (subtaskOpt.isPresent()) {
            return subtaskOpt;
        } else {
            throw new NotFoundException("Подзадача не найдена.");
        }
    }

    @Override
    public void putSubtask(Subtask subtask) throws HasOverlapsException {
        if (!hasOverlap(subtask)) {
            subtask.setId(generateTaskId());
            subtasks.put(subtask.getId(), subtask);
            addTaskInPT(subtask);
        } else {
            throw new HasOverlapsException("Перекрывает другие задачи.");
        }
    }

    @Override
    public void renewSubtask(Subtask newSubtask) throws HasOverlapsException {
        subtasks.remove(newSubtask.getId());
        prioritizedTasks.remove(newSubtask);
        if (!hasOverlap(newSubtask)) {
            subtasks.put(newSubtask.getId(), newSubtask);
            addTaskInPT(newSubtask);
        } else {
            throw new HasOverlapsException("Перекрывает другие задачи.");
        }
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
    public Epic getEpicById(int id) throws NotFoundException {
        historyManager.add(epics.get(id));
        Epic epic = epics.get(id);
        if (epic != null) {
            return epic;
        } else {
            throw new NotFoundException("Эпик не найден.");
        }
    }

    @Override
    public void putEpic(Epic epic) throws HasOverlapsException {
        if (!hasOverlap(epic)) {
            epic.setId(generateTaskId());
            epics.put(epic.getId(), epic);
            for (Subtask subtask : epic.getSubtasks()) {
                subtask.setEpicId(epic.getId());
            }
        } else {
            throw new HasOverlapsException("Перекрывает другие задачи.");
        }
    }

    @Override
    public void renewEpic(Epic newEpic) throws HasOverlapsException {
        epics.remove(newEpic.getId());
        if (!hasOverlap(newEpic)) {
            epics.put(newEpic.getId(), newEpic);
        } else {
            throw new HasOverlapsException("Перекрывает другие задачи.");
        }
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

    private int generateTaskId() {
        return taskId++;
    }

    private void addTaskInPT(Task task) {
        if (task.getStartTime() != null) {
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

    private boolean hasOverlap(Task task) {
        return prioritizedTasks.stream()
                .anyMatch(t -> isOverlap(task, t));
    }
}
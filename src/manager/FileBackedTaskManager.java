package manager;

import exceptions.ManagerSaveException;
import tasks.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;

    public FileBackedTaskManager(File file) throws ManagerSaveException {
        super();
        this.file = file;
        try {
            File parent = file.getParentFile();
            if (parent != null && !parent.exists()) {
                parent.mkdirs();
            }
            file.createNewFile();
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }

    public static FileBackedTaskManager loadFromFile(File file) throws ManagerSaveException {
        FileBackedTaskManager taskManager = new FileBackedTaskManager(file);

        try {
            String text = Files.readString(file.toPath());
            String[] lines = text.split(System.lineSeparator());
            boolean firstLine = true;
            for (String line : lines) {
                if (firstLine) {
                    firstLine = false;
                    continue;
                }
                Task task = CSVFormatter.fromString(line);
                if (task.getId() > taskManager.taskId) {
                    taskManager.taskId = task.getId() + 1;
                }
                switch (task.getType()) {
                    case TaskTypes.TASK:
                        taskManager.tasks.put(task.getId(), task);
                        break;
                    case TaskTypes.EPIC:
                        taskManager.epics.put(task.getId(), (Epic) task);
                        break;
                    case TaskTypes.SUBTASK:
                        taskManager.subtasks.put(task.getId(), (Subtask) task);
                        break;
                    default:
                        throw new IOException();
                }
            }
            for (Subtask subtask : taskManager.getSubtasks()) {
                taskManager.epics.get(subtask.getEpicId()).addSubtask(subtask);
                subtask.setEpic(taskManager.epics.get(subtask.getEpicId()));
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
        return taskManager;
    }

    @Override
    public boolean clearTasks() {
        boolean result = super.clearTasks();
        save();
        return result;
    }

    @Override
    public Task putTask(Task task) {
        Task putTask = super.putTask(task);
        save();
        return putTask;
    }

    @Override
    public Task renewTask(Task newTask) {
        Task renewed = super.renewTask(newTask);
        save();
        return renewed;
    }

    @Override
    public Task removeTaskById(int id) {
        Task task = super.removeTaskById(id);
        save();
        return task;
    }

    @Override
    public boolean clearSubtasks() {
        boolean result = super.clearSubtasks();
        save();
        return result;
    }

    @Override
    public Subtask putSubtask(Subtask subtask) {
        Subtask putSubtask = super.putSubtask(subtask);
        save();
        return putSubtask;
    }

    @Override
    public Subtask renewSubtask(Subtask newSubtask) {
        Subtask renewedSubtask = super.renewSubtask(newSubtask);
        save();
        return renewedSubtask;
    }

    @Override
    public Subtask removeSubtaskById(int id) {
        Subtask subtask = super.removeSubtaskById(id);
        save();
        return subtask;
    }

    @Override
    public boolean clearEpics() {
        boolean result = super.clearEpics();
        save();
        return result;
    }

    @Override
    public Epic putEpic(Epic epic) {
        Epic putEpic = super.putEpic(epic);
        save();
        return putEpic;
    }

    @Override
    public Epic renewEpic(Epic newEpic) {
        Epic renewedEpic = super.renewEpic(newEpic);
        save();
        return renewedEpic;
    }

    @Override
    public Epic removeEpicById(int id) {
        Epic epic = super.removeEpicById(id);
        save();
        return epic;
    }

    private void save() throws ManagerSaveException {
        try (BufferedWriter writer = new BufferedWriter(new FileWriter(file))) {
            // Запись заголовка
            writer.write(CSVFormatter.getHeader());
            writer.newLine();
            // Запись задач
            for (Task task : getTasks()) {
                writer.write(CSVFormatter.toString(task));
                writer.newLine();
            }
            // Запись эпиков
            for (Epic epic : getEpics()) {
                writer.write(CSVFormatter.toString(epic));
                writer.newLine();
            }
            // Запись подзадач
            for (Subtask subtask : getSubtasks()) {
                writer.write(CSVFormatter.toString(subtask));
                writer.newLine();
            }
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }

    public static void main(String[] args) {
        Task task1 = new Task("Task1", "Description", TaskStatuses.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
        Task task2 = new Task("Task2", "Description", TaskStatuses.IN_PROGRESS, Duration.ofMinutes(15),
                LocalDateTime.of(2016, Month.FEBRUARY, 16, 14, 0, 0));
        Epic epic1 = new Epic("Epic1", "Description");
        Epic epic2 = new Epic("Epic2", "Description");
        Subtask subtask1 = new Subtask("Subtask1", "Description",
                TaskStatuses.DONE, epic1, Duration.ofMinutes(15),
                LocalDateTime.of(2016, Month.FEBRUARY, 17, 1, 45, 0));
        Subtask subtask2 = new Subtask("Subtask2", "Description",
                TaskStatuses.IN_PROGRESS, epic2, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 18, 22, 30, 0));

        TaskManager taskManager = Managers.getDefault();
        taskManager.putTask(task1);
        taskManager.putTask(task2);
        taskManager.putEpic(epic1);
        taskManager.putEpic(epic2);
        taskManager.putSubtask(subtask1);
        taskManager.putSubtask(subtask2);

        File file = new File("resources/data.csv");
        TaskManager newTaskManager = FileBackedTaskManager.loadFromFile(file);
        System.out.println(taskManager.getTasks().equals(newTaskManager.getTasks()));
        System.out.println(taskManager.getEpics().equals(newTaskManager.getEpics()));
        System.out.println(taskManager.getSubtasks().equals(newTaskManager.getSubtasks()));

    }
}

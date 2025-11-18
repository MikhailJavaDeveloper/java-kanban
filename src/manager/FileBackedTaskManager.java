package manager;

import exceptions.HasOverlapsException;
import exceptions.ManagerSaveException;
import tasks.*;

import java.io.BufferedWriter;
import java.io.File;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.file.Files;

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
    public void putTask(Task task) throws HasOverlapsException {
        super.putTask(task);
        save();
    }

    @Override
    public void renewTask(Task newTask) throws HasOverlapsException {
        super.renewTask(newTask);
        save();
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
    public void putSubtask(Subtask subtask) throws HasOverlapsException {
        super.putSubtask(subtask);
        save();
    }

    @Override
    public void renewSubtask(Subtask newSubtask) throws HasOverlapsException {
        super.renewSubtask(newSubtask);
        save();
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
    public void putEpic(Epic epic) throws HasOverlapsException {
        super.putEpic(epic);
        save();
    }

    @Override
    public void renewEpic(Epic newEpic) throws HasOverlapsException {
        super.renewEpic(newEpic);
        save();
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
}

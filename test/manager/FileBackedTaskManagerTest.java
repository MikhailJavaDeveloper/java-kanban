package manager;

import exceptions.ManagerSaveException;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatuses;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import static org.junit.jupiter.api.Assertions.*;


public class FileBackedTaskManagerTest {
    @Test
    void shouldSaveEmptyFile() {
        try {
            File.createTempFile("empty", "csv");
            File file = new File("empty.csv");
            TaskManager taskManager1 = new FileBackedTaskManager(file);

            boolean result = Files.readString(file.toPath()).isEmpty();

            assertTrue(result);
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }

    @Test
    void shouldLoadEmptyFile() {
        try {
            File.createTempFile("empty", "csv");
            File file = new File("empty.csv");
            TaskManager taskManager2 = FileBackedTaskManager.loadFromFile(file);

            boolean result = Files.readString(file.toPath()).isEmpty();

            assertTrue(result);
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }

    @Test
    void shouldSaveFewTasks() {
        try {
            File.createTempFile("few-tasks", "csv");
            File file = new File("few-tasks.csv");
            TaskManager taskManager = new FileBackedTaskManager(file);
            Task task = new Task("Task", "Description", TaskStatuses.DONE);
            Epic epic = new Epic("Epic", "Description");
            Subtask subtask = new Subtask("Subtask", "Description", TaskStatuses.IN_PROGRESS, epic);
            String expectedText = "id,type,name,status,description,epic" + System.lineSeparator() +
                    "1,TASK,Task,DONE,Description," + System.lineSeparator() +
                    "2,EPIC,Epic,IN_PROGRESS,Description," + System.lineSeparator() +
                    "3,SUBTASK,Subtask,IN_PROGRESS,Description,2" + System.lineSeparator();

            taskManager.putTask(task);
            taskManager.putEpic(epic);
            taskManager.putSubtask(subtask);
            String result = Files.readString(file.toPath());

            assertEquals(expectedText, result);
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }

    @Test
    void shouldLoadFewTasks() {
        try {
            File.createTempFile("few-tasks", "csv");
            File file = new File("few-tasks.csv");
            TaskManager taskManager1 = new FileBackedTaskManager(file);
            Task task = new Task("Task", "Description", TaskStatuses.DONE);
            Epic epic = new Epic("Epic", "Description");
            Subtask subtask = new Subtask("Subtask", "Description", TaskStatuses.IN_PROGRESS, epic);
            taskManager1.putTask(task);
            taskManager1.putEpic(epic);
            taskManager1.putSubtask(subtask);

            TaskManager taskManager2 = FileBackedTaskManager.loadFromFile(file);

            assertEquals(taskManager1.getTasks(), taskManager2.getTasks());
            assertEquals(taskManager1.getEpics(), taskManager2.getEpics());
            assertEquals(taskManager1.getSubtasks(), taskManager2.getSubtasks());
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }

    @Test
    void shouldRenewTask() {
        try {
            File.createTempFile("temp", "csv");
            File file = new File("temp.csv");
            TaskManager taskManager = new FileBackedTaskManager(file);
            Task oldTask = new Task("OldTask", "OldDescription", TaskStatuses.NEW);
            taskManager.putTask(oldTask);
            Task newTask = new Task(oldTask, "NewTask", "NewDescription", TaskStatuses.DONE);

            taskManager.renewTask(newTask);

            assertEquals(newTask, taskManager.getTaskById(oldTask.getId()));
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }

    @Test
    void shouldRemoveEpicById() {
        try {
            File.createTempFile("temp", "csv");
            File file = new File("temp.csv");
            TaskManager taskManager = new FileBackedTaskManager(file);
            Epic epic = new Epic("Epic", "Description");
            taskManager.putEpic(epic);

            taskManager.removeEpicById(epic.getId());

            assertNull(taskManager.getEpicById(epic.getId()));
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }
}

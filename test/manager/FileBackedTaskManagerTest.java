package manager;

import exceptions.ManagerSaveException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatuses;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;


public class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    @BeforeEach
    void initialiseTaskManager() {
        try {
            File testFile = File.createTempFile("test", ".csv");
            taskManager = new FileBackedTaskManager(testFile);
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }

    @Test
    void shouldSaveEmptyFile() {
        try {
            File file = File.createTempFile("empty", ".csv");
            TaskManager taskManager1 = new FileBackedTaskManager(file);

            boolean result = Files.readString(file.toPath()).isEmpty();

            assertTrue(result, "Пустой файл не сохранился.");
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }

    @Test
    void shouldLoadEmptyFile() {
        try {
            File file = File.createTempFile("empty", ".csv");
            TaskManager taskManager2 = FileBackedTaskManager.loadFromFile(file);

            boolean result = Files.readString(file.toPath()).isEmpty();

            assertTrue(result, "Не загрузил пустой файл.");
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }

    @Test
    void shouldSaveFewTasks() {
        try {
            File file = File.createTempFile("few-tasks", ".csv");
            TaskManager taskManager1 = new FileBackedTaskManager(file);
            Task task = new Task("Task", "Description", TaskStatuses.DONE, Duration.ofMinutes(10),
                    LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
            Epic epic = new Epic("Epic", "Description");
            Subtask subtask = new Subtask("Subtask", "Description",
                    TaskStatuses.IN_PROGRESS, epic, Duration.ofMinutes(15),
                    LocalDateTime.of(2016, Month.FEBRUARY, 16, 14, 0, 0));
            String expectedText = "id,type,name,status,description,epic" + System.lineSeparator() +
                    "1,TASK,Task,DONE,Description,10,2016-02-15T12:30," + System.lineSeparator() +
                    "2,EPIC,Epic,IN_PROGRESS,Description," + System.lineSeparator() +
                    "3,SUBTASK,Subtask,IN_PROGRESS,Description,15,2016-02-16T14:00,2" + System.lineSeparator();

            taskManager1.putTask(task);
            taskManager1.putEpic(epic);
            taskManager1.putSubtask(subtask);
            String result = Files.readString(file.toPath());

            assertEquals(expectedText, result, "Не сохранил несколько задач.");
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }

    @Test
    void shouldLoadFewTasks() {
        try {
            File file = File.createTempFile("few-tasks", ".csv");
            TaskManager taskManager1 = new FileBackedTaskManager(file);
            Task task = new Task("Task", "Description", TaskStatuses.DONE, Duration.ofMinutes(10),
                    LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
            Epic epic = new Epic("Epic", "Description");
            Subtask subtask = new Subtask("Subtask", "Description",
                    TaskStatuses.IN_PROGRESS, epic, Duration.ofMinutes(15),
                    LocalDateTime.of(2016, Month.FEBRUARY, 16, 17, 15, 0));
            taskManager1.putTask(task);
            taskManager1.putEpic(epic);
            taskManager1.putSubtask(subtask);

            TaskManager taskManager2 = FileBackedTaskManager.loadFromFile(file);

            assertEquals(taskManager1.getTasks(), taskManager2.getTasks(), "Не загрузил задачи.");
            assertEquals(taskManager1.getEpics(), taskManager2.getEpics(), "Не загрузил эпики.");
            assertEquals(taskManager1.getSubtasks(), taskManager2.getSubtasks(), "Не загрузил подзадачи.");
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }

    @Test
    void shouldRenewTask() {
        try {
            File file = File.createTempFile("temp", ".csv");
            TaskManager taskManager1 = new FileBackedTaskManager(file);
            Task oldTask = new Task("OldTask", "OldDescription",
                    TaskStatuses.NEW, Duration.ofMinutes(10),
                    LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
            taskManager1.putTask(oldTask);
            Task newTask = new Task(oldTask, "NewTask", "NewDescription",
                    TaskStatuses.DONE, Duration.ofMinutes(15),
                    LocalDateTime.of(2016, Month.FEBRUARY, 16, 14, 45, 0));

            taskManager1.renewTask(newTask);

            assertEquals(newTask, taskManager1.getTaskById(oldTask.getId()), "Не обновил задачу.");
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }

    @Test
    void shouldRemoveEpicById() {
        try {
            File file = File.createTempFile("temp", ".csv");
            TaskManager taskManager1 = new FileBackedTaskManager(file);
            Epic epic = new Epic("Epic", "Description");
            taskManager1.putEpic(epic);

            taskManager1.removeEpicById(epic.getId());

            assertNull(taskManager1.getEpicById(epic.getId()), "Не удалил эпик по id.");
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }

    @Test
    void shouldThrowWhenFileIsNotWritable() {
        try {
            File file = File.createTempFile("not-writable", ".csv");
            file.setReadOnly();

            assertThrows(ManagerSaveException.class, () -> {
                TaskManager taskManager1 = new FileBackedTaskManager(file);
                Task task = new Task("Task", "Desc", TaskStatuses.IN_PROGRESS, Duration.ofMinutes(10),
                        LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0));
                taskManager1.putTask(task);
            }, "Ожидалось исключение ManagerSaveException при попытке сохранить в read-only файл");
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }

    @Test
    void shouldNotThrowWhenFileIsValid() {
        try {
            File file = File.createTempFile("no-throw", ".csv");

            assertDoesNotThrow(() -> new FileBackedTaskManager(file),
                "Не должно быть исключения при корректном файле.");
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }
}

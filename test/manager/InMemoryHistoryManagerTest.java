package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatuses;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    TaskManager taskManager;

    @BeforeEach
    void initialiseTaskManager() {
        taskManager = Managers.getDefault();
    }

    @Test
    void tasksThatAreAddedInHistoryManagerShouldSavePreviousVersionOfTask() {
        Task washDishes = new Task("Помыть посуду", "Нужно нанести мыло для посуды на губку, " +
                "брать посуду одну за другой, намыливать их губкой, " +
                "а затем смывать мыло и класть посуду на место", TaskStatuses.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
        String nameBefore = washDishes.getName();
        String descriptionBefore = washDishes.getDescription();
        TaskStatuses statusBefore = washDishes.getStatus();
        taskManager.putTask(washDishes);

        taskManager.getTaskById(washDishes.getId());
        Task newWashDishes = new Task(washDishes, "А", "Б",
                TaskStatuses.IN_PROGRESS, Duration.ofMinutes(15),
                LocalDateTime.of(2016, Month.FEBRUARY, 16, 14, 30, 0));
        taskManager.renewTask(newWashDishes);
        Task taskFromHistory = taskManager.getHistory().get(0);
        String nameAfter = taskFromHistory.getName();
        String descriptionAfter = taskFromHistory.getDescription();
        TaskStatuses statusAfter = taskFromHistory.getStatus();

        assertEquals(nameBefore, nameAfter, "Имя задачи поменялось.");
        assertEquals(descriptionBefore, descriptionAfter, "Описание задачи поменялось.");
        assertEquals(statusBefore, statusAfter, "Статус задачи поменялся.");
    }

    @Test
    void addTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description",
                TaskStatuses.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
        taskManager.putTask(task);

        taskManager.getTaskById(task.getId());
        Task taskFromHistory = taskManager.getHistory().get(0);

        assertNotNull(taskFromHistory, "Задача не найдена.");
        assertEquals(task, taskFromHistory, "Задачи не совпадают.");
    }

    @Test
    void shouldReturnCorrectListOfTasks() {
        Task task = new Task("Test addNewTask", "Test addNewTask description",
                TaskStatuses.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
        taskManager.putTask(task);
        taskManager.getTaskById(task.getId());
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task);

        List<Task> history = taskManager.getHistory();

        assertEquals(1, history.size(), "Неправильный размер списка.");
        assertEquals(task, history.get(0), "Задачи не совпадают.");
        assertEquals(tasks, history, "Списки не совпадают.");
    }

    @Test
    void shouldRemoveTaskFromHistoryWhenDeletingTask() {
        Task washDishes = new Task("Помыть посуду", "Нужно нанести мыло для посуды на губку, " +
                "брать посуду одну за другой, намыливать их губкой, " +
                "а затем смывать мыло и класть посуду на место", TaskStatuses.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
        taskManager.putTask(washDishes);
        taskManager.getTaskById(washDishes.getId());
        ArrayList<Task> expected = new ArrayList<>();

        taskManager.removeTaskById(washDishes.getId());
        List<Task> resultHistory = taskManager.getHistory();

        assertEquals(expected, resultHistory, "История не пустая.");
    }

    @Test
    void shouldReturnEmptyTaskHistory() {
        List<Task> empty = new ArrayList<>();

        List<Task> result = taskManager.getHistory();

        assertEquals(empty, result, "История не пустая.");
    }

    @Test
    void TaskShouldBeRewrittenIfItIsDuplicated() {
        Task task1 = new Task("Task1", "Desc", TaskStatuses.IN_PROGRESS, Duration.ofMinutes(10),
                LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0));
        Task task2 = new Task("Task2", "Desc", TaskStatuses.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(1970, Month.JANUARY, 1, 2, 0, 0));
        List<Task> expected = List.of(task2, task1);
        taskManager.putTask(task1);
        taskManager.putTask(task2);
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());

        taskManager.getTaskById(task1.getId());
        List<Task> result = taskManager.getHistory();

        assertEquals(expected, result, "Списки задач не совпадают.");
    }

    @Test
    void shouldCorrectlyRemoveTasksFromBeginning() {
        Task task1 = new Task("Task1", "Desc", TaskStatuses.IN_PROGRESS, Duration.ofMinutes(10),
                LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0));
        Task task2 = new Task("Task2", "Desc", TaskStatuses.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(1970, Month.JANUARY, 1, 2, 0, 0));
        Task task3 = new Task("Task3", "Desc", TaskStatuses.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(1970, Month.JANUARY, 1, 4, 0, 0));
        List<Task> expected = List.of(task2, task3);
        taskManager.putTask(task1);
        taskManager.putTask(task2);
        taskManager.putTask(task3);
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());

        taskManager.removeTaskById(task1.getId());
        List<Task> result = taskManager.getHistory();

        assertEquals(expected, result, "Списки задач не совпадают.");
    }

    @Test
    void shouldCorrectlyRemoveTasksFromMiddle() {
        Task task1 = new Task("Task1", "Desc", TaskStatuses.IN_PROGRESS, Duration.ofMinutes(10),
                LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0));
        Task task2 = new Task("Task2", "Desc", TaskStatuses.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(1970, Month.JANUARY, 1, 2, 0, 0));
        Task task3 = new Task("Task3", "Desc", TaskStatuses.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(1970, Month.JANUARY, 1, 4, 0, 0));
        List<Task> expected = List.of(task1, task3);
        taskManager.putTask(task1);
        taskManager.putTask(task2);
        taskManager.putTask(task3);
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());

        taskManager.removeTaskById(task2.getId());
        List<Task> result = taskManager.getHistory();

        assertEquals(expected, result, "Списки задач не совпадают.");
    }

    @Test
    void shouldCorrectlyRemoveTasksFromEnd() {
        Task task1 = new Task("Task1", "Desc", TaskStatuses.IN_PROGRESS, Duration.ofMinutes(10),
                LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0));
        Task task2 = new Task("Task2", "Desc", TaskStatuses.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(1970, Month.JANUARY, 1, 2, 0, 0));
        Task task3 = new Task("Task3", "Desc", TaskStatuses.NEW, Duration.ofMinutes(15),
                LocalDateTime.of(1970, Month.JANUARY, 1, 4, 0, 0));
        List<Task> expected = List.of(task1, task2);
        taskManager.putTask(task1);
        taskManager.putTask(task2);
        taskManager.putTask(task3);
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());
        taskManager.getTaskById(task3.getId());

        taskManager.removeTaskById(task3.getId());
        List<Task> result = taskManager.getHistory();

        assertEquals(expected, result, "Списки задач не совпадают.");
    }
}
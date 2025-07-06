package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import tasks.*;

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
                "а затем смывать мыло и класть посуду на место", TaskStatuses.NEW);
        String nameBefore = washDishes.getName();
        String descriptionBefore = washDishes.getDescription();
        TaskStatuses statusBefore = washDishes.getStatus();
        taskManager.putTask(washDishes);

        taskManager.getTaskById(washDishes.getId());
        Task newWashDishes = new Task(washDishes, "А", "Б",
            TaskStatuses.IN_PROGRESS);
        taskManager.renewTask(newWashDishes);
        Task taskFromHistory = taskManager.getHistoryManager().getHistory()[0];
        String nameAfter = taskFromHistory.getName();
        String descriptionAfter = taskFromHistory.getDescription();
        TaskStatuses statusAfter = taskFromHistory.getStatus();

        assertEquals(nameBefore, nameAfter, "Имя задачи поменялось.");
        assertEquals(descriptionBefore, descriptionAfter, "Описание задачи поменялось.");
        assertEquals(statusBefore, statusAfter, "Статус задачи поменялся.");
    }

    @Test
    void addTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatuses.NEW);
        taskManager.putTask(task);

        taskManager.getTaskById(task.getId());
        Task taskFromHistory = taskManager.getHistoryManager().getHistory()[0];

        assertNotNull(taskFromHistory, "Задача не найдена.");
        assertEquals(task, taskFromHistory, "Задачи не совпадают.");
    }

    @Test
    void shouldReturnCorrectArrayOfTasks() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatuses.NEW);
        taskManager.putTask(task);
        taskManager.getTaskById(task.getId());
        Task taskFromHistory = taskManager.getHistoryManager().getHistory()[0];
        Task[] tasks = new Task[10];
        tasks[0] = task;

        Task[] history = taskManager.getHistoryManager().getHistory();

        assertEquals(10, history.length, "Неправильный размер массива.");
        assertEquals(task, history[0], "Задачи не совпадают.");
        assertArrayEquals(tasks, history, "Массивы не совпадают.");
    }
}
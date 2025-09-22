package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatuses;

import java.util.ArrayList;

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
                "а затем смывать мыло и класть посуду на место", TaskStatuses.NEW);
        String nameBefore = washDishes.getName();
        String descriptionBefore = washDishes.getDescription();
        TaskStatuses statusBefore = washDishes.getStatus();
        taskManager.putTask(washDishes);

        taskManager.getTaskById(washDishes.getId());
        Task newWashDishes = new Task(washDishes, "А", "Б",
            TaskStatuses.IN_PROGRESS);
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
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatuses.NEW);
        taskManager.putTask(task);

        taskManager.getTaskById(task.getId());
        Task taskFromHistory = taskManager.getHistory().get(0);

        assertNotNull(taskFromHistory, "Задача не найдена.");
        assertEquals(task, taskFromHistory, "Задачи не совпадают.");
    }

    @Test
    void shouldReturnCorrectArrayOfTasks() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatuses.NEW);
        taskManager.putTask(task);
        taskManager.getTaskById(task.getId());
        Task taskFromHistory = taskManager.getHistory().get(0);
        ArrayList<Task> tasks = new ArrayList<>();
        tasks.add(task);

        ArrayList<Task> history = taskManager.getHistory();

        assertEquals(1, history.size(), "Неправильный размер массива.");
        assertEquals(task, history.get(0), "Задачи не совпадают.");
        assertEquals(tasks, history, "Массивы не совпадают.");
    }
}
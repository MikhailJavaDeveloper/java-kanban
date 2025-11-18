package manager;

import exceptions.HasOverlapsException;
import exceptions.ManagerSaveException;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatuses;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

public abstract class TaskManagerTest <T extends TaskManager> {
    protected T taskManager;

    @Test
    void shouldCorrectlyDetectOverlap() {
        Task task1 = new Task("Task1", "Desc", TaskStatuses.IN_PROGRESS, Duration.ofMinutes(10),
                LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0));
        Task task2 = new Task("Task2", "Desc", TaskStatuses.IN_PROGRESS, Duration.ofMinutes(10),
                LocalDateTime.of(1970, Month.JANUARY, 1, 0, 5, 0));
        taskManager.putTask(task1);

        assertThrows(HasOverlapsException.class, () -> taskManager.putTask(task2),
                "Ошибка в определении перекрытия задач.");
    }

    @Test
    void shouldReturnCorrectPrioritizedTasksList() {
        Task task1 = new Task("Task1", "Desc", TaskStatuses.IN_PROGRESS, Duration.ofMinutes(10),
                LocalDateTime.of(1970, Month.JANUARY, 1, 2, 0));
        Task task2 = new Task("Task2", "Desc", TaskStatuses.IN_PROGRESS, Duration.ofMinutes(10),
                LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0));
        Task task3 = new Task("Task3", "Desc", TaskStatuses.IN_PROGRESS, Duration.ofMinutes(10),
                LocalDateTime.of(1970, Month.JANUARY, 1, 1, 0));
        List<Task> expected = List.of(task2, task3, task1);
        taskManager.putTask(task1);
        taskManager.putTask(task2);
        taskManager.putTask(task3);

        List<Task> result = taskManager.getPrioritizedTasks();

        assertEquals(expected, result, "Списки задач не совпадают.");
    }
}

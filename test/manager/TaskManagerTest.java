package manager;

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
        List<Task> tasks = List.of(task1);

        taskManager.putTask(task1);
        taskManager.putTask(task2);
        List<Task> result = taskManager.getTasks();

        assertEquals(tasks, result, "Ошибка в определении перекрытия задач.");
    }
}

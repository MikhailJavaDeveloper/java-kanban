package tasks;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class TaskTest {
    @Test
    void tasksShouldBeEqualIfTheirIdIsEqual() {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("А", "Б", TaskStatuses.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
        taskManager.putTask(task1);
        Task task2 = new Task(task1, "В", "Г", TaskStatuses.IN_PROGRESS, Duration.ofMinutes(15),
                LocalDateTime.of(2016, Month.FEBRUARY, 16, 12, 30, 0));

        boolean result = task1.equals(task2);

        assertTrue(result, "Задачи с одинаковым id должны быть равны.");
    }
}
package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import tasks.*;

class TaskTest {
    @Test
    void tasksShouldBeEqualIfTheirIdIsEqual() {
        TaskManager taskManager = Managers.getDefault();
        Task task1 = new Task("А", "Б", TaskStatuses.NEW );
        taskManager.putTask(task1);
        Task task2 = new Task(task1, "В", "Г", TaskStatuses.IN_PROGRESS);

        boolean result = task1.equals(task2);

        assertTrue(result, "Задачи с одинаковым id должны быть равны.");
    }
}
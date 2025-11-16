package manager;

import exceptions.ManagerSaveException;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatuses;

import java.io.IOException;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

public class CSVFormatterTest {
    @Test
    void shouldReturnCorrectStringFromTask() {
        try {
            Task task = new Task("Name", "Description", TaskStatuses.NEW, Duration.ofMinutes(10),
                    LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
            task.setId(1);
            String expectedString = "1,TASK,Name,NEW,Description,10,2016-02-15T12:30,";

            String result = CSVFormatter.toString(task);

            assertEquals(expectedString, result, "Строка из задачи должна быть другая.");
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }

    @Test
    void shouldReturnCorrectTaskFromString() {
        try {
            Task task = new Task("Name", "Description", TaskStatuses.NEW, Duration.ofMinutes(10),
                    LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
            task.setId(1);
            String string = "1,TASK,Name,NEW,Description,10,2016-02-15T12:30,";

            Task result = CSVFormatter.fromString(string);

            assertEquals(task, result, "Задача из строки должна быть другая.");
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }
}

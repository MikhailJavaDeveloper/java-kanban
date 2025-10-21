package manager;

import exceptions.ManagerSaveException;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatuses;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

public class CSVFormatterTest {
    @Test
    void shouldReturnCorrectStringFromTask() {
        try {
            Task task = new Task("Name", "Description", TaskStatuses.NEW);
            task.setId(1);
            String expectedString = "1,TASK,Name,NEW,Description,";

            String result = CSVFormatter.toString(task);

            assertEquals(expectedString, result);
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }

    @Test
    void shouldReturnCorrectTaskFromString() {
        try {
            Task task = new Task("Name", "Description", TaskStatuses.NEW);
            task.setId(1);
            String string = "1,TASK,Name,NEW,Description,";

            Task result = CSVFormatter.fromString(string);

            assertEquals(task, result);
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }
}

package tasks;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import  org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class SubtaskTest {
    TaskManager taskManager;

    @BeforeEach
    void initialiseTaskManager() {
        taskManager = Managers.getDefault();
    }
    @Test
    void subtasksShouldBeEqualIfTheirIdIsEqual() {
        Epic epic = new Epic("Ю", "Я");
        Subtask subtask1 = new Subtask("А", "Б", TaskStatuses.NEW, epic, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
        taskManager.putTask(subtask1);
        Subtask subtask2 = new Subtask(subtask1, "В", "Г", TaskStatuses.DONE, Duration.ofMinutes(15),
                LocalDateTime.of(2016, Month.FEBRUARY, 16, 12, 30, 0));

        boolean result = subtask1.equals(subtask2);

        assertTrue(result, "Подзадачи с одинаковым id должны быть равны.");
    }

    @Test
    void subtaskShouldBeFurtherLinkedToEpicAfterChangingNameDescriptionStatusAndID() {
        Epic buyGroceries = new Epic("Купить продукты", "Купить продкты домой");
        Subtask writeList = new Subtask("Написать список", "Написать список продуктов," +
                " которые нужно купить", TaskStatuses.IN_PROGRESS, buyGroceries, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
        Subtask goToGroceryStore = new Subtask("Пойти в магазин",
                "Пойти в продуктовый магазин и купить там все продукты из списка", TaskStatuses.NEW,
                buyGroceries, Duration.ofMinutes(15),
                LocalDateTime.of(2016, Month.FEBRUARY, 16, 12, 30, 0));
        taskManager.putEpic(buyGroceries);
        taskManager.putSubtask(writeList);
        taskManager.putSubtask(goToGroceryStore);

        writeList.setName("A");
        writeList.setDescription("Б");
        writeList.setStatus(TaskStatuses.NEW);
        writeList.setId(10000);
        boolean result = buyGroceries.getSubtasks().stream()
                .anyMatch(s -> s == writeList);

        assertTrue(result, "Подзадача должна быть дальше привязана к эпику после изменения имени, описания, " +
            "статуса и id.");
    }

    @Test
    void shouldHaveLinkedEpicForSubtask() {
        Epic epic = new Epic("Epic", "Desc");
        Subtask subtask = new Subtask("Subtask", "Desc", TaskStatuses.NEW, epic, Duration.ofMinutes(10),
            LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0));

        assertEquals(epic, subtask.getEpic(), "Подзадача должна ссылаться на правильный эпик.");
    }
}
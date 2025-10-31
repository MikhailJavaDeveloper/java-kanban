package tasks;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;

import static org.junit.jupiter.api.Assertions.*;

class EpicTest {
    @Test
    void epicsShouldBeEqualIfTheirIdIsEqual() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic1 = new Epic("А", "Б");
        taskManager.putEpic(epic1);
        Epic epic2 = new Epic(epic1, "В", "Г");

        boolean result = epic1.equals(epic2);

        assertTrue(result, "Эпики с одинаковым id должны быть равны.");
    }

    @Test
    void statusOfEpicShouldChangeIfStatusOfItsNewSubtaskIsDifferent() {
        Epic refillCarGasTank = new Epic("Пополнить запасы бензина",
            "Пополнить запасы бензина в баке машины");
        TaskStatuses statusBefore = refillCarGasTank.getStatus();

        Subtask fillUpAtGasStation = new Subtask("Заправиться на заправке",
            "Подъехать к колонке, выбрать нужный бензин и заправить бак необходимым количеством бензина",
            TaskStatuses.DONE, refillCarGasTank, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
        TaskStatuses statusAfter = refillCarGasTank.getStatus();

        assertNotEquals(statusBefore, statusAfter,
            "После создания новой подзадачи с отличным от эпика статусом," +
            "статус эпика должен поменяться.");
    }

    @Test
    void statusOfEpicShouldChangeAfterStatusOfItsSubtaskChanges() {
        Epic refillCarGasTank = new Epic("Пополнить запасы бензина",
                "Пополнить запасы бензина в баке машины");
        Subtask fillUpAtGasStation = new Subtask("Заправиться на заправке",
                "Подъехать к колонке, выбрать нужный бензин и заправить бак необходимым количеством бензина",
                TaskStatuses.NEW, refillCarGasTank, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
        TaskStatuses statusBefore = refillCarGasTank.getStatus();

        Subtask newFillUpAtGasStation = new Subtask(fillUpAtGasStation, fillUpAtGasStation.getName(),
                fillUpAtGasStation.getDescription(), TaskStatuses.DONE, Duration.ofMinutes(15),
                LocalDateTime.of(2016, Month.FEBRUARY, 16, 12, 30, 0));
        TaskStatuses statusAfter = refillCarGasTank.getStatus();

        assertNotEquals(statusBefore, statusAfter, "После изменения статуса подзадачи эпика," +
            "статус эпика должен поменяться.");
    }

    @Test
    void shouldBeNoIrrelevantSubtaskIDsLeftInsideEpic() {
        TaskManager taskManager = Managers.getDefault();
        Epic buyGroceries = new Epic("Купить продукты", "Купить продкты домой");
        Subtask writeList = new Subtask("Написать список", "Написать список продуктов," +
                " которые нужно купить", TaskStatuses.IN_PROGRESS, buyGroceries, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
        Subtask goToGroceryStore = new Subtask("Пойти в магазин",
                "Пойти в продуктовый магазин и купить там все продукты из списка", TaskStatuses.NEW,
                buyGroceries, Duration.ofMinutes(16),
                LocalDateTime.of(2016, Month.FEBRUARY, 16, 12, 30, 0));
        taskManager.putEpic(buyGroceries);
        taskManager.putSubtask(writeList);
        taskManager.putSubtask(goToGroceryStore);

        taskManager.removeSubtaskById(writeList.getId());
        boolean result = buyGroceries.getSubtasks().stream()
                        .anyMatch(s -> s.getId() == writeList.getId());

        assertFalse(result, "Внутри эпиков не должно оставаться неактуальных id подзадач.");
    }

    @Test
    void shouldCorrectlyCalculateEpicStatus() {
        LocalDateTime dateTime = LocalDateTime.of(1970, Month.JANUARY, 1, 0, 0, 0);
        Epic epic1 = new Epic("Epic1", "Description");
        Subtask subtask11 = new Subtask("Subtask11", "Desc",
            TaskStatuses.NEW, epic1, Duration.ofMinutes(10), dateTime);
        Subtask subtask12 = new Subtask("Subtask12", "Desc",
                TaskStatuses.NEW, epic1, Duration.ofMinutes(10), dateTime);
        Epic epic2 = new Epic("Epic2", "Description");
        Subtask subtask21 = new Subtask("Subtask21", "Desc",
                TaskStatuses.DONE, epic2, Duration.ofMinutes(10), dateTime);
        Subtask subtask22 = new Subtask("Subtask22", "Desc",
                TaskStatuses.DONE, epic2, Duration.ofMinutes(10), dateTime);
        Epic epic3 = new Epic("Epic3", "Description");
        Subtask subtask31 = new Subtask("Subtask31", "Desc",
                TaskStatuses.NEW, epic3, Duration.ofMinutes(10), dateTime);
        Subtask subtask32 = new Subtask("Subtask32", "Desc",
                TaskStatuses.DONE, epic3, Duration.ofMinutes(10), dateTime);
        Epic epic4 = new Epic("Epic4", "Description");
        Subtask subtask41 = new Subtask("Subtask41", "Desc",
                TaskStatuses.IN_PROGRESS, epic4, Duration.ofMinutes(10), dateTime);
        Subtask subtask42 = new Subtask("Subtask42", "Desc",
                TaskStatuses.IN_PROGRESS, epic4, Duration.ofMinutes(10), dateTime);
        TaskStatuses expected1 = TaskStatuses.NEW;
        TaskStatuses expected2 = TaskStatuses.DONE;
        TaskStatuses expected3 = TaskStatuses.IN_PROGRESS;
        TaskStatuses expected4 = TaskStatuses.IN_PROGRESS;

        TaskStatuses result1 = epic1.getStatus();
        TaskStatuses result2 = epic2.getStatus();
        TaskStatuses result3 = epic3.getStatus();
        TaskStatuses result4 = epic4.getStatus();

        assertEquals(expected1, result1, "Статус эпика рассчитывается неправильно.");
        assertEquals(expected2, result2, "Статус эпика рассчитывается неправильно.");
        assertEquals(expected3, result3, "Статус эпика рассчитывается неправильно.");
        assertEquals(expected4, result4, "Статус эпика рассчитывается неправильно.");
    }
}
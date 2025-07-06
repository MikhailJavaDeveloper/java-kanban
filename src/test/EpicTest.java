package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import tasks.*;

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
    void shouldNotWorkIfEpicIsAddedAsSubtaskToItself() {
        Epic epic = new Epic("А", "Б");
        epic.addSubtask(epic);
    }

    @Test
    void statusOfEpicShouldChangeIfStatusOfItsNewSubtaskIsDifferent() {
        Epic refillCarGasTank = new Epic("Пополнить запасы бензина",
            "Пополнить запасы бензина в баке машины");
        TaskStatuses statusBefore = refillCarGasTank.getStatus();

        Subtask fillUpAtGasStation = new Subtask("Заправиться на заправке",
            "Подъехать к колонке, выбрать нужный бензин и заправить бак необходимым количеством бензина",
            TaskStatuses.DONE, refillCarGasTank);
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
                TaskStatuses.NEW, refillCarGasTank);
        TaskStatuses statusBefore = refillCarGasTank.getStatus();

        Subtask newFillUpAtGasStation = new Subtask(fillUpAtGasStation, fillUpAtGasStation.getName(),
                fillUpAtGasStation.getDescription(), TaskStatuses.DONE);
        TaskStatuses statusAfter = refillCarGasTank.getStatus();

        assertNotEquals(statusBefore, statusAfter, "После изменения статуса подзадачи эпика," +
            "статус эпика должен поменяться.");
    }
}
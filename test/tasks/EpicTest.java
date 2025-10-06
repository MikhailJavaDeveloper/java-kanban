package tasks;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.Test;
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

    @Test
    void shouldBeNoIrrelevantSubtaskIDsLeftInsideEpic() {
        TaskManager taskManager = Managers.getDefault();
        Epic buyGroceries = new Epic("Купить продукты", "Купить продкты домой");
        Subtask writeList = new Subtask("Написать список", "Написать список продуктов," +
                " которые нужно купить", TaskStatuses.IN_PROGRESS, buyGroceries);
        Subtask goToGroceryStore = new Subtask("Пойти в магазин",
                "Пойти в продуктовый магазин и купить там все продукты из списка", TaskStatuses.NEW,
                buyGroceries);
        taskManager.putEpic(buyGroceries);
        taskManager.putSubtask(writeList);
        taskManager.putSubtask(goToGroceryStore);

        taskManager.removeSubtaskById(writeList.getId());
        boolean result = false;
        for (Subtask subtask: buyGroceries.getSubtasks()) {
            if (subtask.getId() == writeList.getId()) result = true;
        }

        assertFalse(result, "Внутри эпиков не должно оставаться неактуальных id подзадач.");
    }
}
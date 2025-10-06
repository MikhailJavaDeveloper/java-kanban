package tasks;

import manager.Managers;
import manager.TaskManager;
import org.junit.jupiter.api.BeforeEach;
import  org.junit.jupiter.api.Test;
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
        Subtask subtask1 = new Subtask("А", "Б", TaskStatuses.NEW, epic);
        taskManager.putTask(subtask1);
        Subtask subtask2 = new Subtask(subtask1, "В", "Г", TaskStatuses.DONE);

        boolean result = subtask1.equals(subtask2);

        assertTrue(result, "Подзадачи с одинаковым id должны быть равны.");
    }

    @Test
    void subtaskShouldBeFurtherLinkedToEpicAfterChangingNameDescriptionStatusAndID() {
        Epic buyGroceries = new Epic("Купить продукты", "Купить продкты домой");
        Subtask writeList = new Subtask("Написать список", "Написать список продуктов," +
                " которые нужно купить", TaskStatuses.IN_PROGRESS, buyGroceries);
        Subtask goToGroceryStore = new Subtask("Пойти в магазин",
                "Пойти в продуктовый магазин и купить там все продукты из списка", TaskStatuses.NEW,
                buyGroceries);
        taskManager.putEpic(buyGroceries);
        taskManager.putSubtask(writeList);
        taskManager.putSubtask(goToGroceryStore);

        writeList.setName("A");
        writeList.setDescription("Б");
        writeList.setStatus(TaskStatuses.NEW);
        writeList.setId(10000);
        boolean result = false;
        for (Subtask subtask: buyGroceries.getSubtasks()) {
            if (subtask == writeList) result = true;
        }

        assertTrue(result, "Подзадача должна быть дальше привязана к эпику после изменения имени, описания, " +
            "статуса и id.");
    }
}
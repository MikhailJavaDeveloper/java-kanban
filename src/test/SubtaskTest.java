package test;

import  org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import tasks.*;

class SubtaskTest {
    @Test
    void subtasksShouldBeEqualIfTheirIdIsEqual() {
        TaskManager taskManager = Managers.getDefault();
        Epic epic = new Epic("Ю", "Я");
        Subtask subtask1 = new Subtask("А", "Б", TaskStatuses.NEW, epic);
        taskManager.putTask(subtask1);
        Subtask subtask2 = new Subtask(subtask1, "В", "Г", TaskStatuses.DONE);

        boolean result = subtask1.equals(subtask2);

        assertTrue(result, "Подзадачи с одинаковым id должны быть равны.");
    }

    @Test
    void shouldNotWorkIfSubtaskIsAddedAsEpicToItself() {
        Subtask subtask = new Subtask("А", "Б", TaskStatuses.DONE, null);
        subtask.setEpic(subtask);
    }
}
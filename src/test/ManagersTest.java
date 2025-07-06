package test;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import tasks.*;

import java.util.ArrayList;

class ManagersTest {
    @Test
    void shouldReturnInitialisedTaskManagerExemplar() {
        TaskManager taskManager = Managers.getDefault();
        ArrayList<Task> tasks = taskManager.getTasks();
        ArrayList<Subtask> subtasks = taskManager.getSubtasks();
        ArrayList<Epic> epics = taskManager.getEpics();

        assertNotNull(tasks, "После создания менеджера задач, список задач должен быть проинициализирован.");
        assertNotNull(subtasks, "После создания менеджера задач," +
            "список подзадач должен быть проинициализирован.");
        assertNotNull(epics, "После создания менеджера задач, список эпиков должен быть проинициализирован.");
    }

    @Test
    void shouldReturnInitialisedHistoryManagerExemplar() {
        HistoryManager historyManager = Managers.getDefaultHistory();
        Task[] history = historyManager.getHistory();

        assertNotNull(history, "После создания менеджера истории просмотров," +
                "список истории просмотров должен быть проинициализирован.");
    }
}
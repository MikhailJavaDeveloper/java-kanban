package test;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import tasks.*;

import java.util.ArrayList;

class InMemoryTaskManagerTest {
    TaskManager taskManager;

    @BeforeEach
    void initialiseTaskManager() {
        taskManager = Managers.getDefault();
    }

    @Test
    void shouldAddTasksOfDifferentType() {
        Task washDishes = new Task("Помыть посуду", "Нужно нанести мыло для посуды на губку, " +
            "брать посуду одну за другой, намыливать их губкой, а затем смывать мыло и класть посуду на место",
            TaskStatuses.NEW);
        Epic refillCarGasTank = new Epic("Пополнить запасы бензина",
            "Пополнить запасы бензина в баке машины");
        Subtask fillUpAtGasStation = new Subtask("Заправиться на заправке",
            "Подъехать к колонке, выбрать нужный бензин и заправить бак необходимым количеством бензина",
            TaskStatuses.NEW, refillCarGasTank);

        taskManager.putTask(washDishes);
        taskManager.putEpic(refillCarGasTank);
        taskManager.putSubtask(fillUpAtGasStation);
        Task task = taskManager.getTaskById(washDishes.getId());
        Epic epic = taskManager.getEpicById(refillCarGasTank.getId());
        Subtask subtask = taskManager.getSubtaskById(fillUpAtGasStation.getId());

        assertNotNull(task, "Задача не найдена.");
        assertNotNull(epic, "Эпик не найден.");
        assertNotNull(subtask, "Эпик не найден.");
        assertEquals(washDishes, task, "Задачи не совпадают.");
        assertEquals(refillCarGasTank, epic,
            "Эпики не совпадают.");
        assertEquals(fillUpAtGasStation, subtask,
            "Подзадачи не совпадают.");
    }

    @Test
    void shouldFindTasksById() {
        Task washDishes = new Task("Помыть посуду", "Нужно нанести мыло для посуды на губку, " +
            "брать посуду одну за другой, намыливать их губкой, " +
            "а затем смывать мыло и класть посуду на место", TaskStatuses.NEW);
        Epic refillCarGasTank = new Epic("Пополнить запасы бензина",
            "Пополнить запасы бензина в баке машины");
        Subtask fillUpAtGasStation = new Subtask("Заправиться на заправке",
            "Подъехать к колонке, выбрать нужный бензин и заправить бак необходимым количеством бензина",
            TaskStatuses.NEW, refillCarGasTank);
        taskManager.putTask(washDishes);
        taskManager.putEpic(refillCarGasTank);
        taskManager.putSubtask(fillUpAtGasStation);

        Task task = taskManager.getTaskById(washDishes.getId());
        Epic epic = taskManager.getEpicById(refillCarGasTank.getId());
        Subtask subtask = taskManager.getSubtaskById(fillUpAtGasStation.getId());

        assertEquals(washDishes, task, "Задача не найдена.");
        assertEquals(refillCarGasTank, epic, "Эпик не найден.");
        assertEquals(fillUpAtGasStation, subtask, "Подзадача не найдена.");
    }

    @Test
    void tasksWithGeneratedIdAndWithSetIdShouldNotConflictInsideManager() {
        Task washDishes = new Task("Помыть посуду", "Нужно нанести мыло для посуды на губку, " +
            "брать посуду одну за другой, намыливать их губкой, " +
            "а затем смывать мыло и класть посуду на место", TaskStatuses.NEW);
        Task newWashDishes = new Task(washDishes, washDishes.getName(), washDishes.getDescription(),
            TaskStatuses.IN_PROGRESS);
        Task assemblePuzzle = new Task("Собрать пазл", "Нужно разложить все пазлины, " +
            "потом совмещать между собой детали которые подходят друг к другу и делают картинку цельной",
            TaskStatuses.IN_PROGRESS);

        taskManager.putTask(newWashDishes);
        taskManager.putTask(assemblePuzzle);
    }

    @Test
    void taskShouldNotChangeIfItIsAddedInTaskManager() {
        Task washDishes = new Task("Помыть посуду", "Нужно нанести мыло для посуды на губку, " +
            "брать посуду одну за другой, намыливать их губкой, а затем смывать мыло и класть посуду на место",
            TaskStatuses.NEW);
        String nameBefore = washDishes.getName();
        String descriptionBefore = washDishes.getDescription();
        TaskStatuses statusBefore = washDishes.getStatus();

        taskManager.putTask(washDishes);
        String nameAfter = washDishes.getName();
        String descriptionAfter = washDishes.getDescription();
        TaskStatuses statusAfter = washDishes.getStatus();

        assertEquals(nameBefore, nameAfter, "Имя задачи поменялось.");
        assertEquals(descriptionBefore, descriptionAfter, "Описание задачи поменялось.");
        assertEquals(statusBefore, statusAfter, "Статус задачи поменялся.");
    }

    @Test
    void addNewTask() {
        Task task = new Task("Test addNewTask", "Test addNewTask description", TaskStatuses.NEW);
        taskManager.putTask(task);

        Task savedTask = taskManager.getTaskById(task.getId());

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        ArrayList<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void getTaskById() {
        Task washDishes = new Task("Помыть посуду", "Нужно нанести мыло для посуды на губку, " +
                "брать посуду одну за другой, намыливать их губкой, " +
                "а затем смывать мыло и класть посуду на место", TaskStatuses.NEW);
        taskManager.putTask(washDishes);

        Task task = taskManager.getTaskById(washDishes.getId());

        assertNotNull(task, "Задача не найдена.");
        assertEquals(washDishes, task, "Задачи не совпадают.");
    }

    @Test
    void removeTaskById() {
        Task washDishes = new Task("Помыть посуду", "Нужно нанести мыло для посуды на губку, " +
                "брать посуду одну за другой, намыливать их губкой, " +
                "а затем смывать мыло и класть посуду на место", TaskStatuses.NEW);
        taskManager.putTask(washDishes);

        taskManager.removeTaskById(washDishes.getId());
        Task task = taskManager.getTaskById(washDishes.getId());

        assertNotEquals(washDishes, task, "Задачи совпадают.");
        assertNull(task, "Задача найдена.");
    }

    @Test
    void renewTask() {
        Task washDishes = new Task("Помыть посуду", "Нужно нанести мыло для посуды на губку, " +
            "брать посуду одну за другой, намыливать их губкой, " +
            "а затем смывать мыло и класть посуду на место", TaskStatuses.NEW);
        taskManager.putTask(washDishes);
        Task newWashDishes = new Task(washDishes, washDishes.getName(), washDishes.getDescription(),
                TaskStatuses.IN_PROGRESS);

        taskManager.renewTask(newWashDishes);
        Task task = taskManager.getTaskById(washDishes.getId());

        assertNotNull(task, "Задача не найдена.");
        assertEquals(newWashDishes, task, "Задачи не совпадают.");
    }

    @Test
    void clearTasks() {
        Task washDishes = new Task("Помыть посуду", "Нужно нанести мыло для посуды на губку, " +
                "брать посуду одну за другой, намыливать их губкой, " +
                "а затем смывать мыло и класть посуду на место", TaskStatuses.NEW);
        taskManager.putTask(washDishes);

        taskManager.clearTasks();
        ArrayList<Task> tasks = taskManager.getTasks();

        assertEquals(0, tasks.size(), "После очищения списка задач, " +
                "размер списка задач должен быть равен 0.");
    }

    @Test
    void getEpicSubtasks() {
        Epic refillCarGasTank = new Epic("Пополнить запасы бензина",
                "Пополнить запасы бензина в баке машины");
        Subtask fillUpAtGasStation = new Subtask("Заправиться на заправке",
                "Подъехать к колонке, выбрать нужный бензин и заправить бак необходимым количеством бензина",
                TaskStatuses.NEW, refillCarGasTank);

        ArrayList<Subtask> subtasks = taskManager.getEpicSubtasks(refillCarGasTank);

        assertEquals(1, subtasks.size(), "Неправильное количество задач.");
        assertEquals(fillUpAtGasStation, subtasks.get(0), "Задачи не совпадают.");
    }
}
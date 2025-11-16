package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TaskStatuses;

import static org.junit.jupiter.api.Assertions.*;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.Month;
import java.util.List;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @BeforeEach
    void initialiseTaskManager() {
        taskManager = new InMemoryTaskManager();
    }

    @Test
    void shouldAddTasksOfDifferentType() {
        Task washDishes = new Task("Помыть посуду", "Нужно нанести мыло для посуды на губку, " +
            "брать посуду одну за другой, намыливать их губкой, а затем смывать мыло и класть посуду на место",
            TaskStatuses.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
        Epic refillCarGasTank = new Epic("Пополнить запасы бензина",
            "Пополнить запасы бензина в баке машины");
        Subtask fillUpAtGasStation = new Subtask("Заправиться на заправке",
            "Подъехать к колонке, выбрать нужный бензин и заправить бак необходимым количеством бензина",
            TaskStatuses.NEW, refillCarGasTank, Duration.ofMinutes(15),
                LocalDateTime.of(2016, Month.FEBRUARY, 16, 14, 30, 0));

        taskManager.putTask(washDishes);
        taskManager.putEpic(refillCarGasTank);
        taskManager.putSubtask(fillUpAtGasStation);
        Task task = taskManager.getTaskById(washDishes.getId()).orElse(null);
        Epic epic = taskManager.getEpicById(refillCarGasTank.getId());
        Subtask subtask = taskManager.getSubtaskById(fillUpAtGasStation.getId()).orElse(null);

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
            "а затем смывать мыло и класть посуду на место", TaskStatuses.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
        Epic refillCarGasTank = new Epic("Пополнить запасы бензина",
            "Пополнить запасы бензина в баке машины");
        Subtask fillUpAtGasStation = new Subtask("Заправиться на заправке",
            "Подъехать к колонке, выбрать нужный бензин и заправить бак необходимым количеством бензина",
            TaskStatuses.NEW, refillCarGasTank, Duration.ofMinutes(15),
                LocalDateTime.of(2016, Month.FEBRUARY, 16, 12, 30, 0));
        taskManager.putTask(washDishes);
        taskManager.putEpic(refillCarGasTank);
        taskManager.putSubtask(fillUpAtGasStation);

        Task task = taskManager.getTaskById(washDishes.getId()).orElse(null);
        Epic epic = taskManager.getEpicById(refillCarGasTank.getId());
        Subtask subtask = taskManager.getSubtaskById(fillUpAtGasStation.getId()).orElse(null);

        assertEquals(washDishes, task, "Задача не найдена.");
        assertEquals(refillCarGasTank, epic, "Эпик не найден.");
        assertEquals(fillUpAtGasStation, subtask, "Подзадача не найдена.");
    }

    @Test
    void tasksWithGeneratedIdAndWithSetIdShouldNotConflictInsideManager() {
        Task washDishes = new Task("Помыть посуду", "Нужно нанести мыло для посуды на губку, " +
            "брать посуду одну за другой, намыливать их губкой, " +
            "а затем смывать мыло и класть посуду на место", TaskStatuses.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
        Task newWashDishes = new Task(washDishes, washDishes.getName(), washDishes.getDescription(),
            TaskStatuses.IN_PROGRESS, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 16, 12, 30, 0));
        Task assemblePuzzle = new Task("Собрать пазл", "Нужно разложить все пазлины, " +
            "потом совмещать между собой детали которые подходят друг к другу и делают картинку цельной",
            TaskStatuses.IN_PROGRESS, Duration.ofMinutes(17),
                LocalDateTime.of(2016, Month.FEBRUARY, 17, 18, 0, 0));

        taskManager.putTask(newWashDishes);
        taskManager.putTask(assemblePuzzle);
    }

    @Test
    void taskShouldNotChangeIfItIsAddedInTaskManager() {
        Task washDishes = new Task("Помыть посуду", "Нужно нанести мыло для посуды на губку, " +
            "брать посуду одну за другой, намыливать их губкой, а затем смывать мыло и класть посуду на место",
            TaskStatuses.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
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
        Task task = new Task("Test addNewTask", "Test addNewTask description",
                TaskStatuses.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
        taskManager.putTask(task);

        Task savedTask = taskManager.getTaskById(task.getId()).orElse(null);

        assertNotNull(savedTask, "Задача не найдена.");
        assertEquals(task, savedTask, "Задачи не совпадают.");

        List<Task> tasks = taskManager.getTasks();

        assertNotNull(tasks, "Задачи не возвращаются.");
        assertEquals(1, tasks.size(), "Неверное количество задач.");
        assertEquals(task, tasks.get(0), "Задачи не совпадают.");
    }

    @Test
    void getTaskById() {
        Task washDishes = new Task("Помыть посуду", "Нужно нанести мыло для посуды на губку, " +
                "брать посуду одну за другой, намыливать их губкой, " +
                "а затем смывать мыло и класть посуду на место", TaskStatuses.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
        taskManager.putTask(washDishes);

        Task task = taskManager.getTaskById(washDishes.getId()).orElse(null);

        assertNotNull(task, "Задача не найдена.");
        assertEquals(washDishes, task, "Задачи не совпадают.");
    }

    @Test
    void removeTaskById() {
        Task washDishes = new Task("Помыть посуду", "Нужно нанести мыло для посуды на губку, " +
                "брать посуду одну за другой, намыливать их губкой, " +
                "а затем смывать мыло и класть посуду на место", TaskStatuses.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
        taskManager.putTask(washDishes);

        taskManager.removeTaskById(washDishes.getId());
        Task task = taskManager.getTaskById(washDishes.getId()).orElse(null);

        assertNotEquals(washDishes, task, "Задачи совпадают.");
        assertNull(task, "Задача найдена.");
    }

    @Test
    void renewTask() {
        Task washDishes = new Task("Помыть посуду", "Нужно нанести мыло для посуды на губку, " +
            "брать посуду одну за другой, намыливать их губкой, " +
            "а затем смывать мыло и класть посуду на место", TaskStatuses.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
        taskManager.putTask(washDishes);
        Task newWashDishes = new Task(washDishes, washDishes.getName(), washDishes.getDescription(),
                TaskStatuses.IN_PROGRESS, Duration.ofMinutes(15),
                LocalDateTime.of(2016, Month.FEBRUARY, 16, 12, 30, 0));

        taskManager.renewTask(newWashDishes);
        Task task = taskManager.getTaskById(washDishes.getId()).orElse(null);

        assertNotNull(task, "Задача не найдена.");
        assertEquals(newWashDishes, task, "Задачи не совпадают.");
    }

    @Test
    void clearTasks() {
        Task washDishes = new Task("Помыть посуду", "Нужно нанести мыло для посуды на губку, " +
                "брать посуду одну за другой, намыливать их губкой, " +
                "а затем смывать мыло и класть посуду на место", TaskStatuses.NEW, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));
        taskManager.putTask(washDishes);

        taskManager.clearTasks();
        List<Task> tasks = taskManager.getTasks();

        assertEquals(0, tasks.size(), "После очищения списка задач, " +
                "размер списка задач должен быть равен 0.");
    }

    @Test
    void getEpicSubtasks() {
        Epic refillCarGasTank = new Epic("Пополнить запасы бензина",
                "Пополнить запасы бензина в баке машины");
        Subtask fillUpAtGasStation = new Subtask("Заправиться на заправке",
                "Подъехать к колонке, выбрать нужный бензин и заправить бак необходимым количеством бензина",
                TaskStatuses.NEW, refillCarGasTank, Duration.ofMinutes(10),
                LocalDateTime.of(2016, Month.FEBRUARY, 15, 12, 30, 0));

        List<Subtask> subtasks = taskManager.getEpicSubtasks(refillCarGasTank);

        assertEquals(1, subtasks.size(), "Неправильное количество задач.");
        assertEquals(fillUpAtGasStation, subtasks.get(0), "Задачи не совпадают.");
    }
}
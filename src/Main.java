import tasks.*;

import java.util.Arrays;

public class Main {

    public static void main(String[] args) {
        Task washDishes = new Task("Помыть посуду", "Нужно нанести мыло для посуды на губку, " +
            "брать посуду одну за другой, намыливать их губкой, " +
            "а затем смывать мыло и класть посуду на место", TaskStatuses.NEW);
        Task assemblePuzzle = new Task("Собрать пазл", "Нужно разложить все пазлины, " +
            "потом совмещать между собой детали которые подходят друг к другу и делают картинку цельной",
            TaskStatuses.IN_PROGRESS);
        Epic buyGroceries = new Epic("Купить продукты", "Купить продкты домой");
        Subtask writeList = new Subtask("Написать список", "Написать список продуктов," +
            " которые нужно купить", TaskStatuses.IN_PROGRESS, buyGroceries);
        Subtask goToGroceryStore = new Subtask("Пойти в магазин",
            "Пойти в продуктовый магазин и купить там все продукты из списка", TaskStatuses.NEW,
            buyGroceries);
        Epic refillCarGasTank = new Epic("Пополнить запасы бензина",
            "Пополнить запасы бензина в баке машины");
        Subtask fillUpAtGasStation = new Subtask("Заправиться на заправке",
            "Подъехать к колонке, выбрать нужный бензин и заправить бак необходимым количеством бензина",
            TaskStatuses.NEW, refillCarGasTank);

        TaskManager taskManager = Managers.getDefault();
        HistoryManager historyManager = taskManager.getHistoryManager();
        taskManager.putTask(washDishes);
        taskManager.putTask(assemblePuzzle);
        taskManager.putEpic(buyGroceries);
        taskManager.putSubtask(writeList);
        taskManager.putSubtask(goToGroceryStore);
        taskManager.putEpic(refillCarGasTank);
        taskManager.putSubtask(fillUpAtGasStation);

        System.out.println("Списки эпиков, задач и подзадач:");
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubtasks());
        System.out.println();


        taskManager.getTaskById(0);
        System.out.println("История просмотров:");
        System.out.println(Arrays.toString(historyManager.getHistory()));
        System.out.println();

        taskManager.getSubtaskById(3);
        System.out.println("История просмотров:");
        System.out.println(Arrays.toString(historyManager.getHistory()));
        System.out.println();

        taskManager.getEpicById(2);
        System.out.println("История просмотров:");
        System.out.println(Arrays.toString(historyManager.getHistory()));
        System.out.println();

        taskManager.getTaskById(0);
        System.out.println("История просмотров:");
        System.out.println(Arrays.toString(historyManager.getHistory()));
        System.out.println();


        Task newWashDishes = new Task(washDishes, washDishes.getName(), washDishes.getDescription(),
            TaskStatuses.IN_PROGRESS);
        Task newAssemblePuzzle = new Task(assemblePuzzle, assemblePuzzle.getName(), assemblePuzzle.getDescription(),
            TaskStatuses.DONE);
        Subtask newWriteList = new Subtask(writeList, writeList.getName(), writeList.getDescription(),
            TaskStatuses.DONE);
        Subtask newGoToGroceryStore = new Subtask(goToGroceryStore, goToGroceryStore.getName(),
            goToGroceryStore.getDescription(), TaskStatuses.IN_PROGRESS);
        Subtask newFillUpAtGasStation = new Subtask(fillUpAtGasStation, fillUpAtGasStation.getName(),
            fillUpAtGasStation.getDescription(), TaskStatuses.DONE);

        taskManager.renewTask(newWashDishes);
        taskManager.renewTask(newAssemblePuzzle);
        taskManager.renewSubtask(newWriteList);
        taskManager.renewSubtask(newGoToGroceryStore);
        taskManager.renewSubtask(newFillUpAtGasStation);

        System.out.println("Списки эпиков, задач и подзадач после изменения статусов у задач и подзадач:");
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubtasks());
        System.out.println();


        taskManager.getTaskById(0);
        System.out.println("История просмотров:");
        System.out.println(Arrays.toString(historyManager.getHistory()));
        System.out.println();

        taskManager.getSubtaskById(4);
        System.out.println("История просмотров:");
        System.out.println(Arrays.toString(historyManager.getHistory()));
        System.out.println();

        taskManager.getEpicById(5);
        System.out.println("История просмотров:");
        System.out.println(Arrays.toString(historyManager.getHistory()));
        System.out.println();


        taskManager.removeTaskById(0);
        taskManager.removeEpicById(2);

        System.out.println("Списки эпиков, задач и подзадач после удаления одной из задач и одного из эпиков:");
        System.out.println(taskManager.getEpics());
        System.out.println(taskManager.getTasks());
        System.out.println(taskManager.getSubtasks());
        System.out.println();


        taskManager.getTaskById(1);
        System.out.println("История просмотров:");
        System.out.println(Arrays.toString(historyManager.getHistory()));
        System.out.println();

        taskManager.getSubtaskById(6);
        System.out.println("История просмотров:");
        System.out.println(Arrays.toString(historyManager.getHistory()));
        System.out.println();

        taskManager.getEpicById(5);
        System.out.println("История просмотров:");
        System.out.println(Arrays.toString(historyManager.getHistory()));
        System.out.println();

        taskManager.getTaskById(1);
        System.out.println("История просмотров:");
        System.out.println(Arrays.toString(historyManager.getHistory()));
        System.out.println();

        taskManager.getEpicById(5);
        System.out.println("История просмотров:");
        System.out.println(Arrays.toString(historyManager.getHistory()));
        System.out.println();

        printAllTasks(taskManager, historyManager);
    }

    private static void printAllTasks(TaskManager manager, HistoryManager historyManager) {
        System.out.println("Задачи:");
        for (Task task : manager.getTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtasks((Epic) epic)) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : historyManager.getHistory()) {
            System.out.println(task);
        }
    }
}
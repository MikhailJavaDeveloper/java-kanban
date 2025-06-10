import tasks.*;

public class Main {

    public static void main(String[] args) {
        Task washDishes = new Task("Помыть посуду", "Нужно нанести мыло для посуды на губку, " +
            "брать посуду одну за другой, намыливать их губкой, " +
            "а затем смывать мыло и класть посуду на место", Statuses.NEW);
        Task assemblePuzzle = new Task("Собрать пазл", "Нужно разложить все пазлины, " +
            "потом совмещать между собой детали которые подходят друг к другу и делают картинку цельной",
            Statuses.IN_PROGRESS);
        Epic buyGroceries = new Epic("Купить продукты", "Купить продкты домой");
        Subtask writeList = new Subtask("Написать список", "Написать список продуктов," +
            " которые нужно купить", Statuses.IN_PROGRESS, buyGroceries);
        Subtask goToGroceryStore = new Subtask("Пойти в магазин",
            "Пойти в продуктовый магазин и купить там все продукты из списка", Statuses.NEW, buyGroceries);
        Epic refillCarGasTank = new Epic("Пополнить запасы бензина",
            "Пополнить запасы бензина в баке машины");
        Subtask fillUpAtGasStation = new Subtask("Заправиться на заправке",
            "Подъехать к колонке, выбрать нужный бензин и заправить бак необходимым количеством бензина",
            Statuses.NEW, refillCarGasTank);

        TasksManager tasksManager = new TasksManager();
        tasksManager.putTask(washDishes);
        tasksManager.putTask(assemblePuzzle);
        tasksManager.putEpic(buyGroceries);
        tasksManager.putSubtask(writeList);
        tasksManager.putSubtask(goToGroceryStore);
        tasksManager.putEpic(refillCarGasTank);
        tasksManager.putSubtask(fillUpAtGasStation);

        System.out.println("Списки эпиков, задач и подзадач:");
        System.out.println(tasksManager.getEpics());
        System.out.println(tasksManager.getTasks());
        System.out.println(tasksManager.getSubtasks());
        System.out.println();

        Task newWashDishes = new Task(washDishes, washDishes.getName(), washDishes.getDescription(),
            Statuses.IN_PROGRESS);
        Task newAssemblePuzzle = new Task(assemblePuzzle, assemblePuzzle.getName(), assemblePuzzle.getDescription(),
            Statuses.DONE);
        Subtask newWriteList = new Subtask(writeList, writeList.getName(), writeList.getDescription(), Statuses.DONE);
        Subtask newGoToGroceryStore = new Subtask(goToGroceryStore, goToGroceryStore.getName(),
            goToGroceryStore.getDescription(), Statuses.IN_PROGRESS);
        Subtask newFillUpAtGasStation = new Subtask(fillUpAtGasStation, fillUpAtGasStation.getName(),
            fillUpAtGasStation.getDescription(), Statuses.DONE);

        tasksManager.renewTask(newWashDishes);
        tasksManager.renewTask(newAssemblePuzzle);
        tasksManager.renewSubtask(newWriteList);
        tasksManager.renewSubtask(newGoToGroceryStore);
        tasksManager.renewSubtask(newFillUpAtGasStation);

        System.out.println("Списки эпиков, задач и подзадач после изменения статусов у задач и подзадач:");
        System.out.println(tasksManager.getEpics());
        System.out.println(tasksManager.getTasks());
        System.out.println(tasksManager.getSubtasks());
        System.out.println();

        tasksManager.removeTaskById(0);
        tasksManager.removeEpicById(2);

        System.out.println("Списки эпиков, задач и подзадач после удаления одной из задач и одного из эпиков:");
        System.out.println(tasksManager.getEpics());
        System.out.println(tasksManager.getTasks());
        System.out.println(tasksManager.getSubtasks());
    }
}

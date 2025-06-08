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
            " которые нужно купить", Statuses.NEW, buyGroceries);
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

        tasksManager.deleteTasks();
        tasksManager.deleteSubtasks();

        Task newWashDishes = new Task("Помыть посуду", "Нужно нанести мыло для посуды на губку, " +
            "брать посуду одну за другой, намыливать их губкой, " +
            "а затем смывать мыло и класть посуду на место", Statuses.IN_PROGRESS);
        Task newAssemblePuzzle = new Task("Собрать пазл", "Нужно разложить все пазлины, " +
            "потом совмещать между собой детали которые подходят друг к другу и делают картинку цельной",
            Statuses.DONE);
        Subtask newWriteList = new Subtask("Написать список", "Написать список продуктов," +
            " которые нужно купить", Statuses.IN_PROGRESS, buyGroceries);
        Subtask newGoToGroceryStore = new Subtask("Пойти в магазин",
            "Пойти в продуктовый магазин и купить там все продукты из списка", Statuses.DONE, buyGroceries);
        Subtask newFillUpAtGasStation = new Subtask("Заправиться на заправке",
            "Подъехать к колонке, выбрать нужный бензин и заправить бак необходимым количеством бензина",
            Statuses.DONE, refillCarGasTank);

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

        tasksManager.deleteTaskById(8);
        tasksManager.deleteEpicById(3);

        System.out.println("Списки эпиков, задач и подзадач после удаления одной из задач и одного из эпиков:");
        System.out.println(tasksManager.getEpics());
        System.out.println(tasksManager.getTasks());
        System.out.println(tasksManager.getSubtasks());
    }
}

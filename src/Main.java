import manager.Managers;
import manager.TaskManager;
import tasks.*;

public class Main {

    public static void main(String[] args) {
        Epic epic1 = new Epic("1", "1");
        Subtask subtask1 = new Subtask("2", "2", TaskStatuses.NEW, epic1);
        Subtask subtask2 = new Subtask("3", "3", TaskStatuses.IN_PROGRESS, epic1);
        Subtask subtask3 = new Subtask("4", "4", TaskStatuses.DONE, epic1);
        Epic epic2 = new Epic("5", "5");

        TaskManager taskManager = Managers.getDefault();
        taskManager.putEpic(epic1);
        taskManager.putSubtask(subtask1);
        taskManager.putSubtask(subtask2);
        taskManager.putSubtask(subtask3);
        taskManager.putEpic(epic2);

        taskManager.getEpicById(5);
        System.out.println("История просмотров:");
        System.out.println(taskManager.getHistory());

        taskManager.getSubtaskById(3);
        System.out.println("История просмотров:");
        System.out.println(taskManager.getHistory());

        taskManager.getEpicById(5);
        System.out.println("История просмотров:");
        System.out.println(taskManager.getHistory());

        taskManager.getSubtaskById(2);
        System.out.println("История просмотров:");
        System.out.println(taskManager.getHistory());

        taskManager.getEpicById(1);
        System.out.println("История просмотров:");
        System.out.println(taskManager.getHistory());

        taskManager.getSubtaskById(2);
        System.out.println("История просмотров:");
        System.out.println(taskManager.getHistory());

        taskManager.getSubtaskById(3);
        System.out.println("История просмотров:");
        System.out.println(taskManager.getHistory());

        taskManager.removeEpicById(5);
        System.out.println("История просмотров:");
        System.out.println(taskManager.getHistory());

        taskManager.removeEpicById(1);
        System.out.println("История просмотров:");
        System.out.println(taskManager.getHistory());
    }
}
package tasks;

import java.util.ArrayList;

public interface TaskManager {
    ArrayList<Task> getTasks();

    boolean clearTasks();

    Task getTaskById(int id);

    Task putTask(Task task);

    Task renewTask(Task newTask);

    Task removeTaskById(int id);


    ArrayList<Subtask> getSubtasks();

    boolean clearSubtasks();

    Subtask getSubtaskById(int id);

    Subtask putSubtask(Subtask subtask);

    Subtask renewSubtask(Subtask newSubtask);

    Subtask removeSubtaskById(int id);


    ArrayList<Epic> getEpics();

    boolean clearEpics();

    Epic getEpicById(int id);

    Epic putEpic(Epic epic);

    Epic renewEpic(Epic newEpic);

    Epic removeEpicById(int id);


    ArrayList<Subtask> getEpicSubtasks(Epic epic);

    HistoryManager getHistoryManager();
}

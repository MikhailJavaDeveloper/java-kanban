package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {
    List<Task> getTasks();

    boolean clearTasks();

    Task getTaskById(int id);

    Task putTask(Task task);

    Task renewTask(Task newTask);

    Task removeTaskById(int id);


    List<Subtask> getSubtasks();

    boolean clearSubtasks();

    Subtask getSubtaskById(int id);

    Subtask putSubtask(Subtask subtask);

    Subtask renewSubtask(Subtask newSubtask);

    Subtask removeSubtaskById(int id);


    List<Epic> getEpics();

    boolean clearEpics();

    Epic getEpicById(int id);

    Epic putEpic(Epic epic);

    Epic renewEpic(Epic newEpic);

    Epic removeEpicById(int id);


    List<Subtask> getEpicSubtasks(Epic epic);

    List<Task> getHistory();
}

package manager;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.util.List;
import java.util.Optional;

public interface TaskManager {
    List<Task> getTasks();

    boolean clearTasks();

    Optional<Task> getTaskById(int id);

    void putTask(Task task);

    void renewTask(Task newTask);

    Task removeTaskById(int id);


    List<Subtask> getSubtasks();

    boolean clearSubtasks();

    Optional<Subtask> getSubtaskById(int id);

    void putSubtask(Subtask subtask);

    void renewSubtask(Subtask newSubtask);

    Subtask removeSubtaskById(int id);


    List<Epic> getEpics();

    boolean clearEpics();

    Epic getEpicById(int id);

    void putEpic(Epic epic);

    void renewEpic(Epic newEpic);

    Epic removeEpicById(int id);


    List<Subtask> getEpicSubtasks(Epic epic);

    List<Task> getHistory();

    List<Task> getPrioritizedTasks();
}

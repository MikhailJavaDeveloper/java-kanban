package manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatuses;

import javax.naming.InsufficientResourcesException;
import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class HistoryApiTest {
    TaskManager taskManager = new InMemoryTaskManager();
    HttpTaskServer taskServer = new HttpTaskServer(taskManager);
    Gson gson = HttpTaskServer.getGson();
    String baseUrl = "http://localhost:" + taskServer.getPort();

    @BeforeEach
    public void setUp() {
        taskManager.clearTasks();
        taskManager.clearSubtasks();
        taskManager.clearEpics();
        taskServer.start();
    }

    @AfterEach
    public void shutDown() {
        taskServer.stop();
    }

    @Test
    public void shouldReturnHistory() throws IOException, InterruptedException {
        Task task1 = new Task("Task1", "Desc", TaskStatuses.IN_PROGRESS, Duration.ofMinutes(10),
                LocalDateTime.of(2021, 12, 1, 12, 12, 7));
        Task task2 = new Task("Task2", "Desc", TaskStatuses.IN_PROGRESS, Duration.ofMinutes(10),
                LocalDateTime.of(2021, 12, 2, 12, 12, 7));
        taskManager.putTask(task1);
        taskManager.putTask(task2);
        taskManager.getTaskById(task1.getId());
        taskManager.getTaskById(task2.getId());

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(baseUrl + "/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код состояния должен быть равен 200.");

        String body = response.body();
        List<Task> history = gson.fromJson(body, new TaskListTypeToken().getType());

        assertEquals(2, history.size(), "Некорректное количество задач.");
        assertEquals("Task1", history.get(0).getName(), "Некорректное имя задачи.");
        assertEquals(List.of(task1, task2), history, "Задачи не совпадают.");
    }

    @Test
    public void shouldReturn405WhenMethodIsWrong() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(baseUrl + "/history");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(405, response.statusCode(), "Код состояния должен быть равен 405.");
    }

    public class TaskListTypeToken extends TypeToken<List<Task>> {

    }
}

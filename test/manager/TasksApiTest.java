package manager;

import com.google.gson.Gson;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Task;
import tasks.TaskStatuses;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class TasksApiTest {
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
    public void addTask() throws IOException, InterruptedException {
        Task task = new Task("Task", "Desc", TaskStatuses.IN_PROGRESS, Duration.ofMinutes(10),
                LocalDateTime.of(2021, 12, 1, 12, 12, 7));
        String taskJson = gson.toJson(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(baseUrl + "/tasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(taskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(201, response.statusCode(), "Код состояния должен быть равен 201.");

        List<Task> tasksFromManager = taskManager.getTasks();
        task.setId(1);

        assertEquals(1, tasksFromManager.size(), "Некорректное количество задач.");
        assertEquals("Task", tasksFromManager.get(0).getName(), "Некорректное имя задачи.");
        assertEquals(List.of(task), tasksFromManager, "Списки задач не совпадают.");
    }

    @Test
    public void shouldReturn404IfTaskNotFound() throws IOException, InterruptedException {
        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(baseUrl + "/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(404, response.statusCode(), "Код состояния должен быть равен 404.");
    }

    @Test
    public void shouldReturnCorrectTask() throws IOException, InterruptedException {
        Task task = new Task("Task", "Desc", TaskStatuses.IN_PROGRESS, Duration.ofMinutes(10),
                LocalDateTime.of(2021, 12, 1, 12, 12, 7));
        taskManager.putTask(task);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(baseUrl + "/tasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код состояния должен быть равен 200.");

        String body = response.body();
        Task taskTest = gson.fromJson(body, Task.class);
        task.setId(1);

        assertEquals("Task", taskTest.getName(), "Некорректное имя задачи.");
        assertEquals(task, taskTest, "Задачи не совпадают.");
    }
}

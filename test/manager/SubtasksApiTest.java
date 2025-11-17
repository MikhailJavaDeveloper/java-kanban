package manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
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

public class SubtasksApiTest {
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
    public void deleteTask() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic", "Desc");
        Subtask subtask = new Subtask("Subtask", "Desc", TaskStatuses.IN_PROGRESS, epic, Duration.ofMinutes(10),
                LocalDateTime.of(2021, 12, 1, 12, 12, 7));
        taskManager.putSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(baseUrl + "/subtasks/1");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .DELETE()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код состояния должен быть равен 200.");

        List<Subtask> subtasksFromManager = taskManager.getSubtasks();

        assertEquals(0, subtasksFromManager.size(), "Некорректное количество задач.");
        assertEquals(List.of(), subtasksFromManager, "Список подзадач не пустой.");
    }

    @Test
    public void shouldReturn406IfThereIsOverlay() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic", "Desc");
        Subtask subtask1 = new Subtask("Subtask1", "Desc",
                TaskStatuses.IN_PROGRESS, epic, Duration.ofMinutes(10),
                LocalDateTime.of(2021, 12, 1, 12, 12, 7));
        Subtask subtask2 = new Subtask("Subtask2", "Desc",
                TaskStatuses.IN_PROGRESS, epic, Duration.ofMinutes(10),
                LocalDateTime.of(2021, 12, 1, 12, 15, 7));
        taskManager.putEpic(epic);
        taskManager.putSubtask(subtask1);
        String subtaskJson = gson.toJson(subtask2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(baseUrl + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(subtaskJson))
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(406, response.statusCode(), "Код состояния должен быть равен 406.");
    }

    @Test
    public void shouldReturnCorrectSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic", "Desc");
        Subtask subtask1 = new Subtask("Subtask1", "Desc",
                TaskStatuses.IN_PROGRESS, epic, Duration.ofMinutes(10),
                LocalDateTime.of(2021, 12, 1, 12, 12, 7));
        Subtask subtask2 = new Subtask("Subtask2", "Desc",
                TaskStatuses.IN_PROGRESS, epic, Duration.ofMinutes(10),
                LocalDateTime.of(2021, 12, 1, 12, 22, 7));
        taskManager.putSubtask(subtask1);
        taskManager.putSubtask(subtask2);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(baseUrl + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код состояния должен быть равен 200.");

        String body = response.body();
        List<Subtask> subtasksFromManager = gson.fromJson(body, new SubtaskListTypeToken().getType());

        assertEquals(2, subtasksFromManager.size(), "Некорректное количество подзадач.");
        assertEquals("Subtask1", subtasksFromManager.get(0).getName(), "Некорректное имя подзадачи.");
        assertEquals(List.of(subtask1, subtask2), subtasksFromManager, "Подзадачи не совпадают.");
    }

    public class SubtaskListTypeToken extends TypeToken<List<Subtask>> {

    }
}

package manager;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import tasks.Epic;
import tasks.Subtask;
import tasks.TaskStatuses;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicsApiTest {
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
    public void shouldReturnEpicSubtasks() throws IOException, InterruptedException {
        Epic epic = new Epic("Epic", "Desc");
        Subtask subtask = new Subtask("Subtask1", "Desc",
                TaskStatuses.IN_PROGRESS, epic, Duration.ofMinutes(10),
                LocalDateTime.of(2021, 12, 1, 12, 12, 7));
        taskManager.putEpic(epic);
        taskManager.putSubtask(subtask);

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(baseUrl + "/epics/1/subtasks");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());

        assertEquals(200, response.statusCode(), "Код состояния должен быть равен 200.");

        String body = response.body();
        List<Subtask> epicSubtasks = gson.fromJson(body, new SubtaskListTypeToken().getType());

        assertEquals(1, epicSubtasks.size(), "Некорректное количество подзадач.");
        assertEquals(List.of(subtask), epicSubtasks, "Списки подзадач не совпадают.");
    }

    @Test
    public void shouldReturn500WhenEpicSubtasksIsNull() throws IOException, InterruptedException {
        String epicJson = """
            {
              "name": "Epic",
              "description": "Desc",
              "subtasks": null
            }
            """;

        HttpClient client = HttpClient.newHttpClient();
        URI url = URI.create(baseUrl + "/epics");
        HttpRequest request = HttpRequest.newBuilder()
                .uri(url)
                .POST(HttpRequest.BodyPublishers.ofString(epicJson))
                .build();

        client.send(request, HttpResponse.BodyHandlers.ofString());

        URI urlTest = URI.create(baseUrl + "/epics/0");
        HttpRequest requestTest = HttpRequest.newBuilder()
                .uri(urlTest)
                .DELETE()
                .build();

        HttpResponse<String> responseTest = client.send(requestTest, HttpResponse.BodyHandlers.ofString());

        assertEquals(500, responseTest.statusCode(), "Код состояния должен быть равен 500.");
    }

    public class SubtaskListTypeToken extends TypeToken<List<Subtask>> {

    }
}

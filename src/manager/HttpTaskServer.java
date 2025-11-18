package manager;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import exceptions.HasOverlapsException;
import exceptions.ManagerSaveException;
import exceptions.NotFoundException;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset UTF_8 = StandardCharsets.UTF_8;
    public TaskManager taskManager;
    private final HttpServer server;
    private static Gson gson;

    public HttpTaskServer() {
        try {
            taskManager = Managers.getDefault();
            server = HttpServer.create(new InetSocketAddress(PORT), 0);
            server.createContext("/tasks", new TasksHandler());
            server.createContext("/subtasks", new SubtasksHandler());
            server.createContext("/epics", new EpicsHandler());
            server.createContext("/history", new HistoryHandler());
            server.createContext("/prioritized", new PrioritizedHandler());
            gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                    .create();
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }

    public HttpTaskServer(TaskManager taskManager) {
        try {
            this.taskManager = taskManager;
            server = HttpServer.create(new InetSocketAddress(0), 0);
            server.createContext("/tasks", new TasksHandler());
            server.createContext("/subtasks", new SubtasksHandler());
            server.createContext("/epics", new EpicsHandler());
            server.createContext("/history", new HistoryHandler());
            server.createContext("/prioritized", new PrioritizedHandler());
            gson = new GsonBuilder()
                    .setPrettyPrinting()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
                    .registerTypeAdapter(Duration.class, new DurationAdapter())
                    .create();
        } catch (IOException e) {
            throw new ManagerSaveException("Произошла неизвестная ошибка");
        }
    }

    public static Gson getGson() {
        return gson;
    }

    public int getPort() {
        return server.getAddress().getPort();
    }

    public void start() {
        server.start();
    }

    public void stop() {
        server.stop(0);
    }

    public static void main(String[] args) {
        HttpTaskServer taskServer = new HttpTaskServer();
        taskServer.start();
    }

    public abstract class BaseHttpHandler implements HttpHandler {
        protected String[] elements;
        protected String method;

        @Override
        public void handle(HttpExchange exchange) throws IOException {
            String path = exchange.getRequestURI().getPath();
            elements = path.split("/");
            method = exchange.getRequestMethod();
        }

        protected void sendOk(HttpExchange exchange) {
            try {
                String resp = "{}";
                byte[] bytes = resp.getBytes(UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                exchange.sendResponseHeaders(200, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            } catch (IOException e) {
                throw new ManagerSaveException("Произошла неизвестная ошибка");
            }
        }

        protected void sendText(HttpExchange exchange, String json) {
            try {
                byte[] resp = json.getBytes(UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                exchange.sendResponseHeaders(200, resp.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(resp);
                }
            } catch (IOException e) {
                throw new ManagerSaveException("Произошла неизвестная ошибка");
            }
        }

        protected void sendCreated(HttpExchange exchange) {
            try {
                byte[] resp = "{}".getBytes(UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                exchange.sendResponseHeaders(201, resp.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(resp);
                }
            } catch (IOException e) {
                throw new ManagerSaveException("Произошла неизвестная ошибка");
            }
        }

        protected void sendNotFound(HttpExchange exchange) {
            try {
                String resp = "{\"error\":\"Not found\"}";
                byte[] bytes = resp.getBytes(UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                exchange.sendResponseHeaders(404, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            } catch (IOException e) {
                throw new ManagerSaveException("Произошла неизвестная ошибка");
            }
        }

        protected void sendMethodNotAllowed(HttpExchange exchange) {
            try {
                String resp = "{\"error\":\"Method not allowed\"}";
                byte[] bytes = resp.getBytes(UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                exchange.sendResponseHeaders(405, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            } catch (IOException e) {
                throw new ManagerSaveException("Произошла неизвестная ошибка");
            }
        }

        protected void sendHasOverlaps(HttpExchange exchange) {
            try {
                String resp = "{\"error\":\"Task overlaps with existing tasks\"}";
                byte[] bytes = resp.getBytes(UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                exchange.sendResponseHeaders(406, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            } catch (IOException e) {
                throw new ManagerSaveException("Произошла неизвестная ошибка");
            }
        }

        protected void sendInternalServerError(HttpExchange exchange) {
            try {
                String resp = "{\"error\":\"Internal server error\"}";
                byte[] bytes = resp.getBytes(UTF_8);
                exchange.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
                exchange.sendResponseHeaders(500, bytes.length);
                try (OutputStream os = exchange.getResponseBody()) {
                    os.write(bytes);
                }
            } catch (IOException e) {
                throw new ManagerSaveException("Произошла неизвестная ошибка");
            }
        }
    }

    public class TasksHandler extends BaseHttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                super.handle(exchange);
                if (elements.length == 2) {
                    switch (method) {
                        case "GET":
                            String json = gson.toJson(taskManager.getTasks());
                            sendText(exchange, json);
                            return;
                        case "POST":
                            InputStream is = exchange.getRequestBody();
                            String body = new String(is.readAllBytes(), UTF_8);

                            Task task = gson.fromJson(body, Task.class);
                            if (task.getId() == 0) {
                                taskManager.putTask(task);
                            } else {
                                taskManager.renewTask(task);
                            }
                            sendCreated(exchange);
                            return;
                        default:
                            sendMethodNotAllowed(exchange);
                            return;
                    }
                } else if (elements.length == 3) {
                    switch (method) {
                        case "GET":
                            Optional<Task> taskOpt = taskManager.getTaskById(Integer.parseInt(elements[2]));
                            Task task = taskOpt.get();
                            String json = gson.toJson(task);
                            sendText(exchange, json);
                            return;
                        case "DELETE":
                            taskManager.removeTaskById(Integer.parseInt(elements[2]));
                            sendOk(exchange);
                            return;
                        default:
                            sendMethodNotAllowed(exchange);
                            return;
                    }
                }
                sendNotFound(exchange);
            } catch (HasOverlapsException e) {
                sendHasOverlaps(exchange);
            } catch (NotFoundException e) {
                sendNotFound(exchange);
            } catch (NullPointerException e) {
                sendInternalServerError(exchange);
            }
        }
    }

    public class SubtasksHandler extends BaseHttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                super.handle(exchange);
                if (elements.length == 2) {
                    switch (method) {
                        case "GET":
                            String json = gson.toJson(taskManager.getSubtasks());
                            sendText(exchange, json);
                            return;
                        case "POST":
                            InputStream is = exchange.getRequestBody();
                            String body = new String(is.readAllBytes(), UTF_8);

                            Subtask subtask = gson.fromJson(body, Subtask.class);
                            Epic epic = taskManager.getEpicById(subtask.getEpicId());
                            if (epic == null) {
                                sendNotFound(exchange); // эпик не найден
                                return;
                            }
                            subtask.setEpic(epic);
                            epic.addSubtask(subtask);
                            if (subtask.getId() == 0) {
                                taskManager.putSubtask(subtask);
                            } else {
                                taskManager.renewSubtask(subtask);
                            }
                            sendCreated(exchange);
                            return;
                        default:
                            sendMethodNotAllowed(exchange);
                            return;
                    }
                } else if (elements.length == 3) {
                    switch (method) {
                        case "GET":
                            Optional<Subtask> subtaskOpt = taskManager.getSubtaskById(Integer.parseInt(elements[2]));
                            Subtask subtask = subtaskOpt.get();
                            String json = gson.toJson(subtask);
                            sendText(exchange, json);
                            return;
                        case "DELETE":
                            taskManager.removeSubtaskById(Integer.parseInt(elements[2]));
                            sendOk(exchange);
                            return;
                        default:
                            sendMethodNotAllowed(exchange);
                            return;
                    }
                }
                sendNotFound(exchange);
            } catch (HasOverlapsException e) {
                sendHasOverlaps(exchange);
            } catch (NotFoundException e) {
                sendNotFound(exchange);
            } catch (NullPointerException e) {
                sendInternalServerError(exchange);
            }
        }
    }

    public class EpicsHandler extends BaseHttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                super.handle(exchange);
                if (elements.length == 2) {
                    switch (method) {
                        case "GET":
                            String json = gson.toJson(taskManager.getEpics());
                            sendText(exchange, json);
                            return;
                        case "POST":
                            InputStream is = exchange.getRequestBody();
                            String body = new String(is.readAllBytes(), UTF_8);

                            Epic epic = gson.fromJson(body, Epic.class);
                            if (epic.getSubtasks() == null) epic.setSubtasks(new ArrayList<>());
                            if (epic.getId() == 0) {
                                taskManager.putEpic(epic);
                            } else {
                                taskManager.renewEpic(epic);
                            }
                            sendCreated(exchange);
                            return;
                        default:
                            sendMethodNotAllowed(exchange);
                            return;
                    }
                } else if (elements.length == 3) {
                    switch (method) {
                        case "GET":
                            Epic epic = taskManager.getEpicById(Integer.parseInt(elements[2]));
                            String json = gson.toJson(epic);
                            sendText(exchange, json);
                            return;
                        case "DELETE":
                            taskManager.removeEpicById(Integer.parseInt(elements[2]));
                            sendOk(exchange);
                            return;
                        default:
                            sendMethodNotAllowed(exchange);
                            return;
                    }
                } else if (elements.length == 4) {
                    switch (method) {
                        case "GET":
                            if (elements[1].equals("epics") && elements[3].equals("subtasks")) {
                                Epic epic = taskManager.getEpicById(Integer.parseInt(elements[2]));
                                List<Subtask> subtasks = taskManager.getEpicSubtasks(epic);
                                String json = gson.toJson(subtasks);
                                sendText(exchange, json);
                                return;
                            }
                        default:
                            sendMethodNotAllowed(exchange);
                            return;
                    }
                }
                sendNotFound(exchange);
            } catch (HasOverlapsException e) {
                sendHasOverlaps(exchange);
            } catch (NotFoundException e) {
                sendNotFound(exchange);
            } catch (NullPointerException e) {
                sendInternalServerError(exchange);
            }
        }
    }

    public class HistoryHandler extends BaseHttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                super.handle(exchange);
                if (elements.length == 2) {
                    switch (method) {
                        case "GET":
                            String json = gson.toJson(taskManager.getHistory());
                            sendText(exchange, json);
                            return;
                        default:
                            sendMethodNotAllowed(exchange);
                            return;
                    }
                }
                sendNotFound(exchange);
            } catch (NullPointerException e) {
                sendInternalServerError(exchange);
            }
        }
    }

    public class PrioritizedHandler extends BaseHttpHandler {
        @Override
        public void handle(HttpExchange exchange) throws IOException {
            try {
                super.handle(exchange);
                if (elements.length == 2) {
                    switch (method) {
                        case "GET":
                            String json = gson.toJson(taskManager.getPrioritizedTasks());
                            sendText(exchange, json);
                            return;
                        default:
                            sendMethodNotAllowed(exchange);
                            return;
                    }
                }
                sendNotFound(exchange);
            } catch (NullPointerException e) {
                sendInternalServerError(exchange);
            }
        }
    }

    public static class LocalDateTimeAdapter extends TypeAdapter<LocalDateTime> {
        private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("dd.MM.yyyy HH:mm:ss");

        @Override
        public void write(final JsonWriter jsonWriter, final LocalDateTime localDateTime) throws IOException {
            jsonWriter.value(localDateTime.format(dtf));
        }

        @Override
        public LocalDateTime read(final JsonReader jsonReader) throws IOException {
            return LocalDateTime.parse(jsonReader.nextString(), dtf);
        }
    }

    public static class DurationAdapter extends TypeAdapter<Duration> {
        @Override
        public void write(final JsonWriter jsonWriter, final Duration duration) throws IOException {
            jsonWriter.value(duration.toMinutes());
        }

        @Override
        public Duration read(JsonReader jsonReader) throws IOException {
            return Duration.ofMinutes(jsonReader.nextLong());
        }
    }
}
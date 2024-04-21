package test;

import com.google.gson.*;
import handlers.LocalDateTimeTypeAdapter;
import managers.Managers;
import managers.TaskManager;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import servers.HttpTaskServer;
import tasks.EpicTask;
import tasks.SubTask;
import tasks.Task;
import tasks.TaskStatus;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class HttpServerTest {
    HttpTaskServer server;
    Task task1;
    SubTask subTask1;
    EpicTask epicTask1;
    private HttpClient client;
    public Gson gson;

    @BeforeEach
    void setUp() throws IOException {
        gson = new GsonBuilder()
                .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                .create();
        client = HttpClient.newBuilder().connectTimeout(Duration.ofSeconds(10)).build();
        server = new HttpTaskServer();
        server.start();
        task1 = new Task("Task 1", "Description 1", TaskStatus.NEW, LocalDateTime.of(2023, 1, 1, 10, 0),
                LocalDateTime.of(2023, 1, 1, 11, 0), 60);
        subTask1 = new SubTask("Subtask 1", "Description 1", TaskStatus.NEW, LocalDateTime.of(2023, 1, 1, 11, 1),
                LocalDateTime.of(2023, 1, 1, 12, 1), 60, 2);
        epicTask1 = new EpicTask("Epic Task 1", "Description 1");
        ArrayList<Integer> subIds = new ArrayList<>(List.of(1));
        epicTask1.setSubIds(subIds);
    }

    @AfterEach
    void endUp() throws IOException {
        server.stop();
        saveSeveralTasks();
    }

    public void saveSeveralTasks() throws IOException {
        FileWriter fwOb = new FileWriter("manager.csv", false);
        PrintWriter pwOb = new PrintWriter(fwOb, false);
        pwOb.flush();
        pwOb.close();
        fwOb.close();
        TaskManager taskManager = Managers.getDefault();
        taskManager.create(subTask1);
        taskManager.create(epicTask1);
        taskManager.create(task1);
        taskManager.getByID(1);
        taskManager.getByID(2);
        taskManager.getByID(3);
    }

    @Test
    public void getTasksTest() throws InterruptedException, IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        task1.setId(3);
        Map<Integer, Task> tasksMap = gson.fromJson(response.body(), new MapTasksTypeToken().getType());
        assertEquals(task1, tasksMap.get(3));

    }

    @Test
    public void getTaskByIdTest() throws InterruptedException, IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/3"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());

        Task task = gson.fromJson(response.body(), Task.class);
        task1.setId(3);
        assertEquals(task1, task);

    }

    @Test
    public void postTaskTest() throws InterruptedException, IOException {
        task1.setDescription("taskdesc");
        task1.setId(0);
        task1.setStartTime(LocalDateTime.of(2024, 1, 1, 11, 1));
        task1.setDuration(task1.getDuration());
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(task1)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertEquals(201, response.statusCode());

    }

    @Test
    public void deleteTaskTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/tasks/3"))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertEquals(201, response.statusCode());
    }

    @Test
    public void postSubTaskTest() throws InterruptedException, IOException {
        subTask1.setId(1);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(subTask1)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertEquals(201, response.statusCode());
    }

    @Test
    public void getSubTaskByIdTest() throws InterruptedException, IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/1"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertFalse(response.body().isBlank());
        SubTask task = gson.fromJson(response.body(), SubTask.class);
        assertEquals(subTask1, task);

    }

    @Test
    public void getSubTasksTest() throws InterruptedException, IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        Map<Integer, SubTask> tasksMap = gson.fromJson(response.body(), new MapSubTasksTypeToken().getType());
        assertEquals(subTask1, tasksMap.get(1));

    }

    @Test
    public void deleteSubTaskTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/1"))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertEquals(201, response.statusCode());
    }

    @Test
    public void postEpicTaskTest() throws InterruptedException, IOException {
        epicTask1.setDescription("taskdesc");
        epicTask1.setId(0);
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .POST(HttpRequest.BodyPublishers.ofString(gson.toJson(epicTask1)))
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertEquals(201, response.statusCode());
    }

    @Test
    public void getEpicTaskByIdTest() throws InterruptedException, IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/2"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        epicTask1.setStartTime(subTask1.getStartTime());
        epicTask1.setId(2);
        epicTask1.setStatus(TaskStatus.NEW);
        epicTask1.setDuration(subTask1.getDuration());
        EpicTask task = gson.fromJson(response.body(), EpicTask.class);
        assertEquals(epicTask1, task);

    }

    @Test
    public void getSubTaskOfEpicByIdTest() throws InterruptedException, IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics/2/subtasks"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        List<SubTask> task = gson.fromJson(response.body(), new SubTasksTypeToken().getType());
        assertEquals(List.of(subTask1), task);

    }

    @Test
    public void getEpicTasksTest() throws InterruptedException, IOException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/epics"))
                .GET()
                .build();

        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        epicTask1.setStartTime(subTask1.getStartTime());
        epicTask1.setId(2);
        epicTask1.setStatus(TaskStatus.NEW);
        epicTask1.setDuration(subTask1.getDuration());
        Map<Integer, EpicTask> tasksMap = gson.fromJson(response.body(), new MapEpicTasksTypeToken().getType());
        assertEquals(epicTask1, tasksMap.get(2));

    }

    @Test
    public void deleteEpicTaskTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/subtasks/2"))
                .DELETE()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        assertEquals(201, response.statusCode());
    }

    @Test
    public void getHistoryTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/history"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        List<Task> history = new ArrayList<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject tempJs = jsonArray.get(i).getAsJsonObject();
            Task task;
            switch (tempJs.get("type").getAsString()) {
                case "TASK":
                    task = gson.fromJson(tempJs.toString(), Task.class);
                    break;
                case "EPIC":
                    task = gson.fromJson(tempJs.toString(), EpicTask.class);
                    break;
                case "SUBTASK":
                    task = gson.fromJson(tempJs.toString(), SubTask.class);
                    break;
                default:
                    task = null;
                    break;
            }
            if (task != null) {
                history.add(task);
            }
        }
        JsonObject subTaskJs = jsonArray.get(2).getAsJsonObject();
        SubTask subTask = gson.fromJson(subTaskJs.toString(), SubTask.class);
        System.out.println(subTask);
        epicTask1.setStartTime(subTask1.getStartTime());
        epicTask1.setId(2);
        epicTask1.setStatus(TaskStatus.NEW);
        epicTask1.setDuration(subTask1.getDuration());
        subTask1.setId(1);
        task1.setId(3);
        List<Task> expected = new ArrayList<>(List.of(subTask1, epicTask1, task1));
        Collections.reverse(expected);
        assertEquals(expected, history);
    }

    @Test
    public void prioritizedTest() throws IOException, InterruptedException {
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create("http://localhost:8080/prioritized"))
                .GET()
                .build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        System.out.println(response.body());
        epicTask1.setStartTime(subTask1.getStartTime());
        epicTask1.setId(2);
        epicTask1.setStatus(TaskStatus.NEW);
        epicTask1.setDuration(subTask1.getDuration());
        subTask1.setId(1);
        task1.setId(3);
        JsonElement jsonElement = JsonParser.parseString(response.body());
        JsonArray jsonArray = jsonElement.getAsJsonArray();
        Set<Task> prioritized = new HashSet<>();
        for (int i = 0; i < jsonArray.size(); i++) {
            JsonObject tempJs = jsonArray.get(i).getAsJsonObject();
            Task task;
            switch (tempJs.get("type").getAsString()) {
                case "TASK":
                    task = gson.fromJson(tempJs.toString(), Task.class);
                    break;
                case "EPIC":
                    task = gson.fromJson(tempJs.toString(), EpicTask.class);
                    break;
                case "SUBTASK":
                    task = gson.fromJson(tempJs.toString(), SubTask.class);
                    break;
                default:
                    task = null;
                    break;
            }
            if (task != null) {
                prioritized.add(task);
            }
        }
        TreeSet<Task> sortedTasksByStartTime = new TreeSet<>(
                (Task task1, Task task2) -> {
                    if (task1.getStartTime() == null) {
                        return 1;
                    }
                    if (task2.getStartTime() == null) {
                        return -1;
                    }
                    return (int) Duration.between(task2.getStartTime(), task1.getStartTime()).toMinutes();
                });
        sortedTasksByStartTime.add(subTask1);
        sortedTasksByStartTime.add(epicTask1);
        sortedTasksByStartTime.add(task1);
        assertEquals(sortedTasksByStartTime, prioritized);
    }
}

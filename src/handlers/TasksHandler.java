package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.TaskException;
import servers.HttpTaskServer;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

public class TasksHandler extends FuncHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String[] uri = exchange.getRequestURI().getPath().split("/");
            String page = "tasks";
            switch (method) {
                case "GET":
                    if (uri.length == 2 && uri[1].equals(page)) {
                        handleGetAllTasks(exchange);
                    } else if (uri.length == 3 && uri[1].equals(page)) {
                        handleGetTaskById(exchange);
                    } else {
                        writeResponse(exchange, WrongUrl, 400);
                    }
                    break;
                case "POST":
                    if (uri.length == 2 && uri[1].equals(page)) {
                        handlePostTask(exchange);
                    } else {
                        writeResponse(exchange, WrongUrl, 400);
                    }
                    break;
                case "DELETE":
                    if (uri.length == 3 && uri[1].equals(page)) {
                        handleDeleteTask(exchange);
                    } else {
                        writeResponse(exchange, WrongUrl, 400);
                    }
                    break;
                default:
                    writeResponse(exchange, WrongUrl, 400);
                    break;
            }
        } catch (Exception e) {
            writeResponse(exchange, ServerErr, 500);
        }
    }


    public void handleGetAllTasks(HttpExchange exchange) throws IOException {
        try {
            Map<Integer, Task> tasks = HttpTaskServer.manager.getTasks();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                    .create();
            String response = gson.toJson(tasks);
            writeResponse(exchange, response, 200);
        } catch (Exception e) {
            writeResponse(exchange, ServerErr, 500);
        }
    }

    public void handleGetTaskById(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> taskIdOpt = getTaskId(exchange);
            if (taskIdOpt.isEmpty()) {
                writeResponse(exchange, WrongId, 400);
                return;
            }
            int taskId = taskIdOpt.get();
            Task task = HttpTaskServer.manager.getByID(taskId);
            if (task != null) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                        .create();
                String response = gson.toJson(task);
                writeResponse(exchange, response, 200);
            } else {
                writeResponse(exchange, IdNotFound, 404);
            }
        } catch (Exception e) {
            writeResponse(exchange, ServerErr, 500);
        }

    }

    public void handlePostTask(HttpExchange exchange) throws IOException {
        try {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                    .create();
            Task task = gson.fromJson(body, Task.class);
            if (task.getId() != 0) {
                HttpTaskServer.manager.update(task);
            } else {
                HttpTaskServer.manager.create(task);
            }
            writeResponse(exchange, Added, 201);
        } catch (TaskException e) {
            writeResponse(exchange, ManagerErr, 406);
        } catch (Exception e) {
            writeResponse(exchange, ServerErr, 500);
        }
    }

    public void handleDeleteTask(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> taskIdOpt = getTaskId(exchange);
            if (taskIdOpt.isEmpty()) {
                writeResponse(exchange, WrongId, 400);
                return;
            }
            int taskId = taskIdOpt.get();
            HttpTaskServer.manager.deleteById(taskId);
            writeResponse(exchange, Deleted, 201);
        } catch (Exception e) {
            writeResponse(exchange, ServerErr, 500);
        }
    }
}

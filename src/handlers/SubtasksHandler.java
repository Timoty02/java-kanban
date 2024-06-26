package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.TaskException;
import servers.HttpTaskServer;
import tasks.SubTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Map;
import java.util.Optional;

public class SubtasksHandler extends FuncHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String[] uri = exchange.getRequestURI().getPath().split("/");
            String page = "subtasks";
            switch (method) {
                case "GET":
                    if (uri.length == 2 && uri[1].equals(page)) {
                        handleGetAllSubTasks(exchange);
                    } else if (uri.length == 3 && uri[1].equals(page)) {
                        handleGetSubTaskById(exchange);
                    } else {
                        writeResponse(exchange, WrongUrl, 400);
                    }
                    break;
                case "POST":
                    if (uri.length == 2 && uri[1].equals(page)) {
                        handlePostSubTask(exchange);
                    } else {
                        writeResponse(exchange, WrongUrl, 400);
                    }
                    break;
                case "DELETE":
                    if (uri.length == 3 && uri[1].equals(page)) {
                        handleDeleteSubTask(exchange);
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


    public void handleGetAllSubTasks(HttpExchange exchange) throws IOException {
        try {
            Map<Integer, SubTask> subtasks = HttpTaskServer.manager.getSubTasks();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                    .create();
            String response = gson.toJson(subtasks);
            writeResponse(exchange, response, 200);
        } catch (Exception e) {
            writeResponse(exchange, ServerErr, 500);
        }
    }

    public void handleGetSubTaskById(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> subtaskIdOpt = getTaskId(exchange);
            if (subtaskIdOpt.isEmpty()) {
                writeResponse(exchange, WrongId, 400);
                return;
            }
            int subtaskId = subtaskIdOpt.get();
            SubTask subtask = (SubTask) HttpTaskServer.manager.getByID(subtaskId);
            if (subtask != null) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                        .create();
                String response = gson.toJson(subtask);
                writeResponse(exchange, response, 200);
            } else {
                writeResponse(exchange, IdNotFound, 404);
            }
        } catch (Exception e) {
            writeResponse(exchange, ServerErr, 500);
        }

    }

    public void handlePostSubTask(HttpExchange exchange) throws IOException {
        try {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                    .create();
            SubTask subtask = gson.fromJson(body, SubTask.class);
            if (subtask.getId() != 0) {
                HttpTaskServer.manager.update(subtask);
            } else {
                HttpTaskServer.manager.create(subtask);
            }
            writeResponse(exchange, Added, 201);
        } catch (TaskException e) {
            writeResponse(exchange, ManagerErr, 406);
        } catch (Exception e) {
            writeResponse(exchange, ServerErr, 500);
        }
    }

    public void handleDeleteSubTask(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> subtaskIdOpt = getTaskId(exchange);
            if (subtaskIdOpt.isEmpty()) {
                writeResponse(exchange, WrongId, 400);
                return;
            }
            int subtaskId = subtaskIdOpt.get();
            HttpTaskServer.manager.deleteById(subtaskId);
            writeResponse(exchange, Deleted, 201);
        } catch (Exception e) {
            writeResponse(exchange, ServerErr, 500);
        }
    }
}

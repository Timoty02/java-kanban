package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.TaskException;
import servers.HttpTaskServer;
import tasks.SubTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class SubtasksHandler extends FuncHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String[] uri = exchange.getRequestURI().getPath().split("/");
            switch (method) {
                case "GET":
                    if (uri.length == 2 && uri[1].equals("subtasks")) {
                        handleGetAllSubTasks(exchange);
                    } else if (uri.length == 3 && uri[1].equals("subtasks")) {
                        handleGetSubTaskById(exchange);
                    } else {
                        writeResponse(exchange, "Некорректный URL", 400);
                    }
                    break;
                case "POST":
                    if (uri.length == 2 && uri[1].equals("subtasks")) {
                        handlePostSubTask(exchange);
                    } else {
                        writeResponse(exchange, "Некорректный URL", 400);
                    }
                    break;
                case "DELETE":
                    if (uri.length == 3 && uri[1].equals("subtasks")) {
                        handleDeleteSubTask(exchange);
                    } else {
                        writeResponse(exchange, "Некорректный URL", 400);
                    }
                    break;
                default:
                    writeResponse(exchange, "Некорректный URL", 400);
                    break;
            }
        } catch (Exception e){
            writeResponse(exchange, "Произошла ошибка сервера", 500);
        }
        //HttpsubtaskServer.stop();
    }


    public void handleGetAllSubTasks(HttpExchange exchange) throws IOException {
        try {
            Map<Integer, SubTask> subtasks = HttpTaskServer.manager.getSubTasks();
            Gson gson = new Gson();
            String response = gson.toJson(subtasks);
            writeResponse(exchange, response, 200);
        } catch (Exception e) {
            writeResponse(exchange, "Произошла ошибка сервера", 500);
        }
    }

    public void handleGetSubTaskById(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> subtaskIdOpt = getTaskId(exchange);
            if (subtaskIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int subtaskId = subtaskIdOpt.get();
            SubTask subtask = (SubTask) HttpTaskServer.manager.getByID(subtaskId);
            if (subtask != null) {
                Gson gson = new Gson();
                String response = gson.toJson(subtask);
                writeResponse(exchange, response, 200);
            } else {
                writeResponse(exchange, "Задача с данным id не найдена", 404);
            }
        } catch (Exception e) {
            writeResponse(exchange, "Произошла ошибка сервера", 500);
        }

    }

    public void handlePostSubTask(HttpExchange exchange) throws IOException {
        try {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            SubTask subtask = gson.fromJson(body, SubTask.class);
            if (subtask.getId() != 0) {
                HttpTaskServer.manager.update(subtask);
            } else {
                HttpTaskServer.manager.create(subtask);
            }
            writeResponse(exchange, "Задача успешно добавлена", 201);
        } catch (TaskException e) {
            writeResponse(exchange, "Добавляемая задача пересекается с текущими", 406);
        } catch (Exception e) {
            writeResponse(exchange, "Произошла ошибка сервера", 500);
        }
    }

    public void handleDeleteSubTask(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> subtaskIdOpt = getTaskId(exchange);
            if (subtaskIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int subtaskId = subtaskIdOpt.get();
            HttpTaskServer.manager.deleteById(subtaskId);
            writeResponse(exchange, "Задача успешно удалена", 201);
        } catch (Exception e) {
            writeResponse(exchange, "Произошла ошибка сервера", 500);
        }
    }
}

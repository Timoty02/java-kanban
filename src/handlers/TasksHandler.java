package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.TaskException;
import servers.HttpTaskServer;
import tasks.Task;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Map;
import java.util.Optional;

public class TasksHandler extends FuncHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        String[] uri = exchange.getRequestURI().getPath().split("/");
        switch (method) {
            case "GET":
                if (uri.length == 2 && uri[1].equals("tasks")) {
                    handleGetAllTasks(exchange);
                } else if (uri.length == 3 && uri[1].equals("tasks")) {
                    handleGetTaskById(exchange);
                } else {
                    writeResponse(exchange, "Некорректный URL", 400);
                }
                break;
            case "POST":
                if (uri.length == 2 && uri[1].equals("tasks")) {
                    handlePostTask(exchange);
                } else {
                    writeResponse(exchange, "Некорректный URL", 400);
                }
                break;
            case "DELETE":
                if (uri.length == 3 && uri[1].equals("tasks")) {
                    handleDeleteTask(exchange);
                } else {
                    writeResponse(exchange, "Некорректный URL", 400);
                }
                break;
            default:
                writeResponse(exchange, "Некорректный URL", 400);
                break;
        }
        //HttpTaskServer.stop();
    }


    public void handleGetAllTasks(HttpExchange exchange) throws IOException {
        try {
            Map<Integer, Task> tasks = HttpTaskServer.manager.getTasks();
            Gson gson = new Gson();
            String response = gson.toJson(tasks);
            writeResponse(exchange, response, 200);
        } catch (Exception e) {
            writeResponse(exchange, "Произошла ошибка сервера", 500);
        }
    }

    public void handleGetTaskById(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> taskIdOpt = getTaskId(exchange);
            if (taskIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int taskId = taskIdOpt.get();
            Task task = HttpTaskServer.manager.getByID(taskId);
            if (task != null) {
                Gson gson = new Gson();
                String response = gson.toJson(task);
                writeResponse(exchange, response, 200);
            } else {
                writeResponse(exchange, "Задача с данным id не найдена", 404);
            }
        } catch (Exception e) {
            writeResponse(exchange, "Произошла ошибка сервера", 500);
        }

    }

    public void handlePostTask(HttpExchange exchange) throws IOException {
        try {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Gson gson = new Gson();
            Task task = gson.fromJson(body, Task.class);
            if (task.getId() != 0) {
                HttpTaskServer.manager.update(task);
            } else {
                HttpTaskServer.manager.create(task);
            }
            writeResponse(exchange, "Задача успешно добавлена", 201);
        } catch (TaskException e) {
            writeResponse(exchange, "Добавляемая задача пересекается с текущими", 406);
        } catch (Exception e) {
            writeResponse(exchange, "Произошла ошибка сервера", 500);
        }
    }

    public void handleDeleteTask(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> taskIdOpt = getTaskId(exchange);
            if (taskIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int taskId = taskIdOpt.get();
            HttpTaskServer.manager.deleteById(taskId);
            writeResponse(exchange, "Задача успешно удалена", 201);
        } catch (Exception e) {
            writeResponse(exchange, "Произошла ошибка сервера", 500);
        }
    }
}

package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import exceptions.TaskException;
import servers.HttpTaskServer;
import tasks.EpicTask;
import tasks.SubTask;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Optional;

public class EpicsHandler extends FuncHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            String[] uri = exchange.getRequestURI().getPath().split("/");
            switch (method) {
                case "GET":
                    if (uri.length == 2 && uri[1].equals("epics")) {
                        handleGetAllEpicTasks(exchange);
                    } else if (uri.length == 3 && uri[1].equals("epics")) {
                        handleGetEpicTaskById(exchange);
                    } else if (uri.length == 4 && uri[1].equals("epics") && uri[3].equals("subtasks")) {
                        handleGetSubtasksOfEpicTask(exchange);
                    } else {
                        writeResponse(exchange, "Некорректный URL", 400);
                    }
                    break;
                case "POST":
                    if (uri.length == 2 && uri[1].equals("epics")) {
                        handlePostEpicTask(exchange);
                    } else {
                        writeResponse(exchange, "Некорректный URL", 400);
                    }
                    break;
                case "DELETE":
                    if (uri.length == 3 && uri[1].equals("epics")) {
                        handleDeleteEpicTask(exchange);
                    } else {
                        writeResponse(exchange, "Некорректный URL", 400);
                    }
                    break;
                default:
                    writeResponse(exchange, "Некорректный URL", 400);
                    break;
            }
        } catch (Exception e) {
            writeResponse(exchange, "Произошла ошибка сервера", 500);
        }
        //HttpepictaskServer.stop();
    }


    public void handleGetAllEpicTasks(HttpExchange exchange) throws IOException {
        try {
            Map<Integer, EpicTask> epictasks = HttpTaskServer.manager.getEpicTasks();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                    .create();
            String response = gson.toJson(epictasks);
            writeResponse(exchange, response, 200);
        } catch (Exception e) {
            writeResponse(exchange, "Произошла ошибка сервера", 500);
        }
    }

    public void handleGetEpicTaskById(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> epicTaskIdOpt = getTaskId(exchange);
            if (epicTaskIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int epicTaskId = epicTaskIdOpt.get();
            EpicTask epictask = (EpicTask) HttpTaskServer.manager.getByID(epicTaskId);
            if (epictask != null) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                        .create();
                String response = gson.toJson(epictask);
                writeResponse(exchange, response, 200);
            } else {
                writeResponse(exchange, "Задача с данным id не найдена", 404);
            }
        } catch (Exception e) {
            writeResponse(exchange, "Произошла ошибка сервера", 500);
        }

    }

    public void handlePostEpicTask(HttpExchange exchange) throws IOException {
        try {
            String body = new String(exchange.getRequestBody().readAllBytes(), StandardCharsets.UTF_8);
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                    .create();
            EpicTask epictask = gson.fromJson(body, EpicTask.class);
            if (epictask.getId() != 0) {
                HttpTaskServer.manager.update(epictask);
            } else {
                HttpTaskServer.manager.create(epictask);
            }
            writeResponse(exchange, "Задача успешно добавлена", 201);
        } catch (TaskException e) {
            writeResponse(exchange, "Добавляемая задача пересекается с текущими", 406);
        } catch (Exception e) {
            writeResponse(exchange, "Произошла ошибка сервера", 500);
        }
    }

    public void handleGetSubtasksOfEpicTask(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> epicTaskIdOpt = getTaskId(exchange);
            if (epicTaskIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int epicTaskId = epicTaskIdOpt.get();
            List<SubTask> subTasks = HttpTaskServer.manager.getSubsOfEpic(epicTaskId);
            if (subTasks != null) {
                Gson gson = new GsonBuilder()
                        .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                        .create();
                String response = gson.toJson(subTasks);
                writeResponse(exchange, response, 200);
            } else {
                writeResponse(exchange, "Задача с данным id не найдена", 404);
            }
        } catch (Exception e) {
            writeResponse(exchange, "Произошла ошибка сервера", 500);
        }
    }

    public void handleDeleteEpicTask(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> epicTaskIdOpt = getTaskId(exchange);
            if (epicTaskIdOpt.isEmpty()) {
                writeResponse(exchange, "Некорректный идентификатор задачи", 400);
                return;
            }
            int epicTaskId = epicTaskIdOpt.get();
            HttpTaskServer.manager.deleteById(epicTaskId);
            writeResponse(exchange, "Задача успешно удалена", 201);
        } catch (Exception e) {
            writeResponse(exchange, "Произошла ошибка сервера", 500);
        }
    }
}

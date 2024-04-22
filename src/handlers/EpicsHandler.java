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
            String page = "epics";
            switch (method) {
                case "GET":
                    if (uri.length == 2 && uri[1].equals(page)) {
                        handleGetAllEpicTasks(exchange);
                    } else if (uri.length == 3 && uri[1].equals(page)) {
                        handleGetEpicTaskById(exchange);
                    } else if (uri.length == 4 && uri[1].equals(page) && uri[3].equals("subtasks")) {
                        handleGetSubtasksOfEpicTask(exchange);
                    } else {
                        writeResponse(exchange, WrongUrl, 400);
                    }
                    break;
                case "POST":
                    if (uri.length == 2 && uri[1].equals(page)) {
                        handlePostEpicTask(exchange);
                    } else {
                        writeResponse(exchange, WrongUrl, 400);
                    }
                    break;
                case "DELETE":
                    if (uri.length == 3 && uri[1].equals(page)) {
                        handleDeleteEpicTask(exchange);
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


    public void handleGetAllEpicTasks(HttpExchange exchange) throws IOException {
        try {
            Map<Integer, EpicTask> epictasks = HttpTaskServer.manager.getEpicTasks();
            Gson gson = new GsonBuilder()
                    .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                    .create();
            String response = gson.toJson(epictasks);
            writeResponse(exchange, response, 200);
        } catch (Exception e) {
            writeResponse(exchange, ServerErr, 500);
        }
    }

    public void handleGetEpicTaskById(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> epicTaskIdOpt = getTaskId(exchange);
            if (epicTaskIdOpt.isEmpty()) {
                writeResponse(exchange, WrongId, 400);
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
                writeResponse(exchange, IdNotFound, 404);
            }
        } catch (Exception e) {
            writeResponse(exchange, ServerErr, 500);
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
            writeResponse(exchange, Added, 201);
        } catch (TaskException e) {
            writeResponse(exchange, ManagerErr, 406);
        } catch (Exception e) {
            writeResponse(exchange, ServerErr, 500);
        }
    }

    public void handleGetSubtasksOfEpicTask(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> epicTaskIdOpt = getTaskId(exchange);
            if (epicTaskIdOpt.isEmpty()) {
                writeResponse(exchange, WrongId, 400);
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
                writeResponse(exchange, IdNotFound, 404);
            }
        } catch (Exception e) {
            writeResponse(exchange, ServerErr, 500);
        }
    }

    public void handleDeleteEpicTask(HttpExchange exchange) throws IOException {
        try {
            Optional<Integer> epicTaskIdOpt = getTaskId(exchange);
            if (epicTaskIdOpt.isEmpty()) {
                writeResponse(exchange, WrongId, 400);
                return;
            }
            int epicTaskId = epicTaskIdOpt.get();
            HttpTaskServer.manager.deleteById(epicTaskId);
            writeResponse(exchange, Deleted, 201);
        } catch (Exception e) {
            writeResponse(exchange, ServerErr, 500);
        }
    }
}

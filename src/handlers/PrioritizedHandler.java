package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import servers.HttpTaskServer;
import tasks.Task;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.TreeSet;

public class PrioritizedHandler extends FuncHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            if (method.equals("GET")) {
                TreeSet<Task> taskTreeSet = HttpTaskServer.manager.getSortedTasksByStartTime();
                if (taskTreeSet != null && !taskTreeSet.isEmpty()) {
                    Gson gson = new GsonBuilder()
                            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeTypeAdapter())
                            .create();
                    writeResponse(exchange, gson.toJson(taskTreeSet), 200);
                } else {
                    writeResponse(exchange, "", 200);
                }
            } else {
                writeResponse(exchange, "Некорректный запрос", 400);
            }
        } catch (Exception e) {
            writeResponse(exchange, "Произошла ошибка сервера", 500);
        }

    }
}

package handlers;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import servers.HttpTaskServer;
import tasks.Task;

import java.io.IOException;
import java.util.List;

public class HistoryHandler extends FuncHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        try {
            String method = exchange.getRequestMethod();
            if (method.equals("GET")){
                List<Task> history = HttpTaskServer.manager.getHistory();
                if (history!=null && !history.isEmpty()){
                    Gson gson = new Gson();
                    writeResponse(exchange, gson.toJson(history), 200);
                } else {
                    writeResponse(exchange, "", 200);
                }
            } else {
                writeResponse(exchange, "Некорректный запрос", 400);
            }
        } catch (Exception e){
            writeResponse(exchange,"Произошла ошибка сервера", 500);
        }

    }
}

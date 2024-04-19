package handlers;

import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import servers.HttpTaskServer;
import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;

public class TasksHandler extends FuncHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String method = exchange.getRequestMethod();
        if (method.equals("GET")){
            String[] uri = exchange.getRequestURI().getPath().split("/");
            if (uri.length == 2 && uri[1].equals("tasks")){
                handleGetAllTasks(exchange);
            } else if (uri.length == 3 && uri[1].equals("tasks")) {

            }
        }
    }

    public void handleGetAllTasks(HttpExchange exchange) throws IOException {

    }

    public void handleGetTaskById(HttpExchange exchange) throws IOException {

    }

    public void handlePostTask(HttpExchange exchange) throws IOException {

    }


}

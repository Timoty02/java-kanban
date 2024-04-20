package servers;

import com.sun.net.httpserver.HttpServer;
import handlers.TasksHandler;
import managers.Managers;
import managers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;

public class HttpTaskServer {
    public static TaskManager manager;
    private static final int PORT = 8080;
    private static HttpServer httpServer;

    public static void main(String[] args) throws IOException {
        manager = Managers.getDefault();
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public static void stop() {
        httpServer.stop(10);
    }
}

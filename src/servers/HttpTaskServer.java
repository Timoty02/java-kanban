package servers;

import com.sun.net.httpserver.HttpServer;
import handlers.PrioritizedHandler;
import handlers.SubtasksHandler;
import handlers.TasksHandler;
import managers.Managers;
import managers.TaskManager;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.util.Scanner;

public class HttpTaskServer {
    public static TaskManager manager;
    private static final int PORT = 8080;
    private static HttpServer httpServer;

    public static void main(String[] args) throws IOException {
        manager = Managers.getDefault();
        httpServer = HttpServer.create(new InetSocketAddress(PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.createContext("/subtasks", new SubtasksHandler());
        httpServer.createContext("/prioritized", new PrioritizedHandler());
        httpServer.start();
        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        System.out.println("Чтобы остановить напечайте s");
        Scanner scanner = new Scanner(System.in);
        String code = scanner.next();
        while (!code.equals("s")){
            System.out.println("Чтобы остановить напечайте s");
            code = scanner.next();
        }
        stop();
    }

    public static void stop() {
        httpServer.stop(10);
    }
}

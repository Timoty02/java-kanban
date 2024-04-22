package handlers;

import com.sun.net.httpserver.HttpExchange;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.Optional;

abstract class FuncHandler {
    protected static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected static final String WrongUrl = "Некорректный URL";
    protected static final String ServerErr = "Произошла ошибка сервера";
    protected static final String WrongId = "Некорректный идентификатор задачи";
    protected static final String IdNotFound = "Задача с данным id не найдена";
    protected static final String Added = "Задача успешно добавлена";
    protected static final String ManagerErr = "Добавляемая задача пересекается с текущими";
    protected static final String WrongRequest = "Некорректный запрос";
    protected static final String Deleted = "Задача успешно удалена";

    protected void writeResponse(HttpExchange exchange, String responseString, int responseCode) throws IOException {
        try (OutputStream os = exchange.getResponseBody()) {
            exchange.sendResponseHeaders(responseCode, 0);
            os.write(responseString.getBytes(DEFAULT_CHARSET));
        }
        exchange.close();
    }

    protected Optional<Integer> getTaskId(HttpExchange exchange) {
        String[] pathParts = exchange.getRequestURI().getPath().split("/");
        try {
            return Optional.of(Integer.parseInt(pathParts[2]));
        } catch (NumberFormatException exception) {
            return Optional.empty();
        }
    }


}


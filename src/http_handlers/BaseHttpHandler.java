package http_handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import model.Task;
import util.DurationAdapter;
import util.LocalDateTimeAdapter;

import java.io.IOException;
import java.io.OutputStream;
import java.nio.charset.StandardCharsets;
import java.nio.charset.Charset;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public abstract class BaseHttpHandler implements HttpHandler {
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    protected static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .serializeNulls()
            .excludeFieldsWithoutExposeAnnotation()
            .create();

    protected String getMethod(HttpExchange h) {
        return h.getRequestMethod();
    }

    protected String[] getURLElements(HttpExchange h) {
        return h.getRequestURI().getPath().split("/");
    }

    protected String getLastURLElement(HttpExchange h) {
        String[] elements = getURLElements(h);
        return elements[elements.length - 1];
    }

    protected Boolean hasIdInUrl(String lastUrlElement) {
        try {
            Integer.parseInt(lastUrlElement);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    protected JsonObject getJsonObject(HttpExchange h) throws IOException {
        return gson.fromJson(new String(h.getRequestBody().readAllBytes()), JsonObject.class);
    }

    protected void baseResponse(HttpExchange h, int statusCode, String text) throws IOException {
        byte[] resp = text.getBytes(DEFAULT_CHARSET);
        h.getResponseHeaders().add("Content-Type", "application/json;charset=utf-8");
        h.sendResponseHeaders(statusCode, resp.length);
        try (OutputStream os = h.getResponseBody()) {
            os.write(resp);
        }
    }

    protected void sendText(HttpExchange h, String text) throws IOException {
        baseResponse(h, 200, text);
    }

    protected void sendNotFound(HttpExchange h, String text) throws IOException {
        String responseText;
        responseText = Objects.requireNonNullElseGet(text,
                () -> String.format("Указанный ресурс не найден: %s", h.getRequestURI()));
        baseResponse(h, 404, responseText);
    }

    protected void sendTaskCreated(HttpExchange h, Task task) throws IOException {
        baseResponse(h, 201, gson.toJson(task));
    }

    protected void sendHasInteractions(HttpExchange h, Task task) throws IOException {
        baseResponse(h, 406, String.format("У задачи %s есть пересечение по времени", task.toString()));
    }

    protected void sendForbiddenMethod(HttpExchange h) throws IOException {
        baseResponse(h, 405, String.format("Метод %s не поддерживается", h.getRequestMethod()));
    }
}

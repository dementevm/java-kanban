package handlers;

import com.sun.net.httpserver.HttpExchange;
import controller.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public HistoryHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (getMethod(exchange).equals("GET")) {
            if (getLastURLElement(exchange).equals("history")) {
                sendText(exchange, gson.toJson(taskManager.getHistory()));
            } else {
                sendNotFound(exchange, null);
            }
        } else {
            sendForbiddenMethod(exchange);
        }
    }
}

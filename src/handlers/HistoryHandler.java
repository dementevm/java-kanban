package handlers;

import com.sun.net.httpserver.HttpExchange;
import controller.TaskManager;

import java.io.IOException;

public class HistoryHandler extends BaseHttpHandler {

    public HistoryHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void handleGetRequest(HttpExchange exchange) throws IOException {
        if (getLastURLElement(exchange).equals("history")) {
            sendText(exchange, gson.toJson(taskManager.getHistory()));
        } else {
            sendNotFound(exchange, null);
        }
    }
}

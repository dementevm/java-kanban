package handlers;

import com.sun.net.httpserver.HttpExchange;
import controller.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler {

    public PrioritizedHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void handleGetRequest(HttpExchange exchange) throws IOException {
        if (getLastURLElement(exchange).equals("prioritized")) {
            sendText(exchange, gson.toJson(taskManager.getPrioritizedTasks()));
        } else {
            sendNotFound(exchange, null);
        }
    }
}

package handlers;

import com.sun.net.httpserver.HttpExchange;
import controller.TaskManager;

import java.io.IOException;

public class PrioritizedHandler extends BaseHttpHandler {
    TaskManager taskManager;

    public PrioritizedHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        if (getMethod(exchange).equals("GET")) {
            if (getLastURLElement(exchange).equals("prioritized")) {
                sendText(exchange, gson.toJson(taskManager.getPrioritizedTasks()));
            } else {
                sendNotFound(exchange, null);
            }
        } else {
            sendForbiddenMethod(exchange);
        }
    }
}

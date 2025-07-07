package handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import controller.TaskManager;
import exceptions.TaskCreateError;
import exceptions.TaskNotFound;
import model.Task;
import util.TaskStatus;

import java.io.IOException;
import java.util.ArrayList;

public class TaskHandler extends BaseHttpHandler {
    public TaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void handleGetRequest(HttpExchange exchange) throws IOException {
        String lasUrlElement = getLastURLElement(exchange);
        if (hasIdInUrl(lasUrlElement)) {
            int taskId = Integer.parseInt(lasUrlElement);
            try {
                Task task = taskManager.getTask(taskId);
                sendText(exchange, gson.toJson(task));
            } catch (TaskNotFound e) {
                sendNotFound(exchange, String.format("Задача с id %d не найдена", taskId));
            }
        } else {
            if (lasUrlElement.equals("tasks")) {
                ArrayList<Task> tasks = taskManager.getTasks();
                String json = gson.toJson(tasks);
                sendText(exchange, json);
            } else {
                sendNotFound(exchange, null);
            }
        }
    }

    @Override
    protected void handlePostRequest(HttpExchange exchange) throws IOException {
        JsonObject taskJson = getJsonObject(exchange);
        JsonElement id = taskJson.get("taskId");
        Task task = gson.fromJson(taskJson, Task.class);
        try {
            if (id == null || id.getAsInt() == 0) {
                if (task.getStatus() == null) {
                    task.setStatus(TaskStatus.NEW);
                }
                taskManager.createTask(task);
                sendTaskCreated(exchange, task);
            } else {
                taskManager.updateTask(task);
                sendTaskCreated(exchange, task);
            }
        } catch (TaskCreateError e) {
            sendHasInteractions(exchange, task);
        }
    }

    @Override
    protected void handleDeleteRequest(HttpExchange exchange) throws IOException {
        String lasUrlElement = getLastURLElement(exchange);
        if (hasIdInUrl(lasUrlElement)) {
            int taskId = Integer.parseInt(lasUrlElement);
            try {
                taskManager.deleteTask(taskId);
                sendText(exchange, String.format("Задача с id %d удалена", taskId));
            } catch (TaskNotFound e) {
                sendNotFound(exchange, String.format("Задача с id %d не найдена", taskId));
            }
        } else {
            sendNotFound(exchange, null);
        }
    }
}

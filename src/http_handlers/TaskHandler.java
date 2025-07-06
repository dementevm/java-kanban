package http_handlers;

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
    private final TaskManager taskManager;

    public TaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String lasUrlElement = getLastURLElement(exchange);
        switch (getMethod(exchange)) {
            case "GET": {
                if (hasIdInUrl(lasUrlElement)) {
                    getTask(exchange, Integer.parseInt(lasUrlElement));
                } else {
                    if (lasUrlElement.equals("tasks")) {
                        getAllTasks(exchange);
                    } else {
                        sendNotFound(exchange, null);
                    }
                }
                break;
            }
            case "POST": {
                createOrUpdateTask(exchange);
                break;
            }
            case "DELETE": {
                if (hasIdInUrl(lasUrlElement)) {
                    deleteTask(exchange, Integer.parseInt(lasUrlElement));
                } else {
                    sendNotFound(exchange, null);
                }
                break;
            }
            default:
                sendForbiddenMethod(exchange);
        }
    }

    public void getTask(HttpExchange exchange, int id) throws IOException {
        try {
            Task task = taskManager.getTask(id);
            sendText(exchange, gson.toJson(task));
        } catch (TaskNotFound e) {
            sendNotFound(exchange, String.format("Задача с id %d не найдена", id));
        }
    }

    public void getAllTasks(HttpExchange exchange) throws IOException {
        ArrayList<Task> tasks = taskManager.getTasks();
        String json = gson.toJson(tasks);
        sendText(exchange, json);
    }

    public void createOrUpdateTask(HttpExchange exchange) throws IOException {
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

    public void deleteTask(HttpExchange exchange, int taskId) throws IOException {
        try {
            taskManager.deleteTask(taskId);
            sendText(exchange, String.format("Задача с id %d удалена", taskId));
        } catch (TaskNotFound e) {
            sendNotFound(exchange, String.format("Задача с id %d не найдена", taskId));
        }
    }
}

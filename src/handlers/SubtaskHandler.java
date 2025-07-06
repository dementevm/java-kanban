package handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import controller.TaskManager;
import exceptions.TaskCreateError;
import exceptions.TaskNotFound;
import model.Subtask;
import util.TaskStatus;

import java.io.IOException;
import java.util.ArrayList;

public class SubtaskHandler extends BaseHttpHandler {
    private final TaskManager taskManager;

    public SubtaskHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String lasUrlElement = getLastURLElement(exchange);
        switch (getMethod(exchange)) {
            case "GET": {
                if (hasIdInUrl(lasUrlElement)) {
                    getSubtask(exchange, Integer.parseInt(lasUrlElement));
                } else {
                    if (lasUrlElement.equals("subtasks")) {
                        getAllSubtasks(exchange);
                    } else {
                        sendNotFound(exchange, null);
                    }
                }
                break;
            }
            case "POST": {
                createOrUpdateSubtask(exchange);
                break;
            }
            case "DELETE": {
                if (hasIdInUrl(lasUrlElement)) {
                    deleteSubtask(exchange, Integer.parseInt(lasUrlElement));
                } else {
                    sendNotFound(exchange, null);
                }
                break;
            }
            default:
                sendForbiddenMethod(exchange);
        }
    }

    public void getSubtask(HttpExchange exchange, int id) throws IOException {
        try {
            Subtask subtask = taskManager.getSubtask(id);
            sendText(exchange, gson.toJson(subtask));
        } catch (TaskNotFound e) {
            sendNotFound(exchange, String.format("Subtask с id %d не найдена", id));
        }
    }

    public void getAllSubtasks(HttpExchange exchange) throws IOException {
        ArrayList<Subtask> subtasks = taskManager.getSubtasks();
        String json = gson.toJson(subtasks);
        sendText(exchange, json);
    }

    public void createOrUpdateSubtask(HttpExchange exchange) throws IOException {
        JsonObject taskJson = getJsonObject(exchange);
        JsonElement id = taskJson.get("taskId");
        JsonElement epicID = taskJson.get("epicId");
        if (epicID == null) {
            baseResponse(exchange, 400, "Subtask не может быть создан без epicID");
        }
        Subtask subtask = gson.fromJson(taskJson, Subtask.class);
        try {
            if (id == null || id.getAsInt() == 0) {
                if (subtask.getStatus() == null) {
                    subtask.setStatus(TaskStatus.NEW);
                }
                taskManager.createSubtask(subtask);
                sendTaskCreated(exchange, subtask);
            } else {
                taskManager.updateSubtask(subtask);
                sendTaskCreated(exchange, subtask);
            }
        } catch (TaskCreateError | TaskNotFound e) {
            if (e.getClass() == TaskCreateError.class) {
                sendHasInteractions(exchange, subtask);
            } else {
                sendNotFound(exchange, "Не найден Epic с ID - " + epicID);
            }
        }
    }

    public void deleteSubtask(HttpExchange exchange, int taskId) throws IOException {
        try {
            taskManager.deleteSubtask(taskId);
            sendText(exchange, String.format("Subtask с id %d удален", taskId));
        } catch (TaskNotFound e) {
            sendNotFound(exchange, String.format("Subtask с id %d не найден", taskId));
        }
    }
}

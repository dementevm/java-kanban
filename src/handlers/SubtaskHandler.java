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

    public SubtaskHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void handleGetRequest(HttpExchange exchange) throws IOException {
        String lasUrlElement = getLastURLElement(exchange);
        if (hasIdInUrl(lasUrlElement)) {
            int id = Integer.parseInt(lasUrlElement);
            try {
                Subtask subtask = taskManager.getSubtask(id);
                sendText(exchange, gson.toJson(subtask));
            } catch (TaskNotFound e) {
                sendNotFound(exchange, String.format("Subtask с id %d не найдена", id));
            }
        } else {
            if (lasUrlElement.equals("subtasks")) {
                ArrayList<Subtask> subtasks = taskManager.getSubtasks();
                String json = gson.toJson(subtasks);
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

    @Override
    protected void handleDeleteRequest(HttpExchange exchange) throws IOException {
        String lasUrlElement = getLastURLElement(exchange);
        if (hasIdInUrl(lasUrlElement)) {
            int taskId = Integer.parseInt(lasUrlElement);
            try {
                taskManager.deleteSubtask(taskId);
                sendText(exchange, String.format("Subtask с id %d удален", taskId));
            } catch (TaskNotFound e) {
                sendNotFound(exchange, String.format("Subtask с id %d не найден", taskId));
            }
        } else {
            sendNotFound(exchange, null);
        }
    }
}

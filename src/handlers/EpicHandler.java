package handlers;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.sun.net.httpserver.HttpExchange;
import controller.TaskManager;
import exceptions.TaskCreateError;
import exceptions.TaskNotFound;
import model.Epic;
import model.Subtask;

import java.io.IOException;
import java.util.ArrayList;

public class EpicHandler extends BaseHttpHandler {
    public EpicHandler(TaskManager taskManager) {
        super(taskManager);
    }

    @Override
    protected void handleGetRequest(HttpExchange exchange) throws IOException {
        String[] urlElements = getURLElements(exchange);
        String lasUrlElement = urlElements[urlElements.length - 1];
        String penultimateElement = urlElements[urlElements.length - 2];
        if (hasIdInUrl(lasUrlElement)) {
            int id = Integer.parseInt(lasUrlElement);
            try {
                Epic epic = taskManager.getEpic(id);
                sendText(exchange, gson.toJson(epic));
            } catch (TaskNotFound e) {
                sendNotFound(exchange, String.format("Epic с id %d не найден", id));
            }
        } else if (lasUrlElement.equals("subtasks") && hasIdInUrl(penultimateElement)) {
            int id = Integer.parseInt(penultimateElement);
            try {
                Epic epic = taskManager.getEpic(id);
                ArrayList<Subtask> subtasks = epic.getSubtasks();
                String json = gson.toJson(subtasks);
                sendText(exchange, json);
            } catch (TaskNotFound e) {
                sendNotFound(exchange, String.format("Epic с id %d не найден", id));
            }
        } else {
            if (lasUrlElement.equals("epics")) {
                ArrayList<Epic> epics = taskManager.getEpics();
                String json = gson.toJson(epics);
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
        Epic epic = gson.fromJson(taskJson, Epic.class);
        try {
            if (id == null || id.getAsInt() == 0) {
                if (taskJson.get("epicSubtasks") == null) {
                    epic.setEpicSubtasks();
                }
                taskManager.createEpic(epic);
                sendTaskCreated(exchange, epic);
            } else {
                taskManager.updateEpic(epic);
                sendTaskCreated(exchange, epic);
            }
        } catch (TaskCreateError e) {
            sendHasInteractions(exchange, epic);
        }
    }

    @Override
    protected void handleDeleteRequest(HttpExchange exchange) throws IOException {
        if (hasIdInUrl(getLastURLElement(exchange))) {
            int epicId = Integer.parseInt(getLastURLElement(exchange));
            try {
                taskManager.deleteEpic(epicId);
                sendText(exchange, String.format("Epic с id %d удален", epicId));
            } catch (TaskNotFound e) {
                sendNotFound(exchange, String.format("Epic с id %d не найден", epicId));
            }
        } else {
            sendNotFound(exchange, null);
        }
    }
}

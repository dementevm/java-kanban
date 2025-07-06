package http_handlers;

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
    private final TaskManager taskManager;

    public EpicHandler(TaskManager taskManager) {
        this.taskManager = taskManager;
    }

    @Override
    public void handle(HttpExchange exchange) throws IOException {
        String[] urlElements = getURLElements(exchange);
        String lasUrlElement = urlElements[urlElements.length - 1];
        String penultimateElement = urlElements[urlElements.length - 2];
        switch (getMethod(exchange)) {
            case "GET": {
                if (hasIdInUrl(lasUrlElement)) {
                    getEpic(exchange, Integer.parseInt(lasUrlElement));
                } else if (lasUrlElement.equals("subtasks") && hasIdInUrl(penultimateElement)) {
                    getEpicSubtasks(exchange, Integer.parseInt(penultimateElement));
                } else {
                    if (lasUrlElement.equals("epics")) {
                        getAllEpics(exchange);
                    } else {
                        sendNotFound(exchange, null);
                    }
                }
                break;
            }
            case "POST": {
                createOrUpdateEpic(exchange);
                break;
            }
            case "DELETE": {
                if (hasIdInUrl(getLastURLElement(exchange))) {
                    deleteEpic(exchange, Integer.parseInt(lasUrlElement));
                } else {
                    sendNotFound(exchange, null);
                }
                break;
            }
            default:
                sendForbiddenMethod(exchange);
        }
    }

    public void getEpic(HttpExchange exchange, int id) throws IOException {
        try {
            Epic epic = taskManager.getEpic(id);
            sendText(exchange, gson.toJson(epic));
        } catch (TaskNotFound e) {
            sendNotFound(exchange, String.format("Epic с id %d не найден", id));
        }
    }

    public void getEpicSubtasks(HttpExchange exchange, int id) throws IOException {
        try {
            Epic epic = taskManager.getEpic(id);
            ArrayList<Subtask> subtasks = epic.getSubtasks();
            String json = gson.toJson(subtasks);
            sendText(exchange, json);
        } catch (TaskNotFound e) {
            sendNotFound(exchange, String.format("Epic с id %d не найден", id));
        }
    }

    public void getAllEpics(HttpExchange exchange) throws IOException {
        ArrayList<Epic> epics = taskManager.getEpics();
        String json = gson.toJson(epics);
        sendText(exchange, json);
    }

    public void createOrUpdateEpic(HttpExchange exchange) throws IOException {
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

    public void deleteEpic(HttpExchange exchange, int epicId) throws IOException {
        try {
            taskManager.deleteEpic(epicId);
            sendText(exchange, String.format("Epic с id %d удален", epicId));
        } catch (TaskNotFound e) {
            sendNotFound(exchange, String.format("Epic с id %d не найден", epicId));
        }
    }
}

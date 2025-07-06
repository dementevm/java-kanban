package http_handlers;

import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class EpicHandlerTest extends BaseHttpHandlerTest {

    @Test
    public void testGetEpics() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/epics");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Epic> epicsFromResponse = gson.fromJson(response.body(), epicListType);
        assertEquals(manager.getEpics(), epicsFromResponse);
    }

    @Test
    public void testGetEpic() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/epics/4");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Epic epicsFromResponse = gson.fromJson(response.body(), Epic.class);
        assertEquals(manager.getEpics().getFirst(), epicsFromResponse);
    }

    @Test
    public void testCreateEpic() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/epics");
        Epic newEic = new Epic("Epic3", "Description");
        String newEpicJson = gson.toJson(newEic);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(
                HttpRequest.BodyPublishers.ofString(newEpicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Epic> epicsFromManager = manager.getEpics();
        assertEquals(3, epicsFromManager.size(), "Некорректное количество задач");
    }

    @Test
    void testUpdateEpic() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/epics");
        Epic epic = manager.getEpics().getFirst();
        Epic updatedEpic = new Epic("Epic update", "Descr", epic.getTaskId(), epic.getStatus());
        String updatedEpicJson = gson.toJson(updatedEpic);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(
                HttpRequest.BodyPublishers.ofString(updatedEpicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        Task epicFromManager = manager.getEpics().getFirst();
        assertEquals(updatedEpic.getTaskName(), epicFromManager.getTaskName());
    }

    @Test
    void testGetEpicSubtasks() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/epics/4/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> epicSubtasks = gson.fromJson(response.body(), subtaskListType);
        assertEquals(manager.getEpics().getFirst().getSubtasks(), epicSubtasks);

    }

    @Test
    void testDeleteEpic() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/epics/4");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(1, manager.getEpics().size());
    }
}

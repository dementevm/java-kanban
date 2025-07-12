package handlers;

import model.Subtask;
import model.Task;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class SubtaskHandlerTest extends BaseHttpHandlerTest {

    @Test
    public void testGetSubtasks() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/subtasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Subtask> epicsFromResponse = gson.fromJson(response.body(), subtaskListType);
        assertEquals(manager.getSubtasks(), epicsFromResponse);
    }

    @Test
    public void testGetSubtask() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/subtasks/6");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Subtask subtasksFromJson = gson.fromJson(response.body(), Subtask.class);
        assertEquals(manager.getSubtasks().getFirst(), subtasksFromJson);
    }

    @Test
    public void testAddSubtask() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/subtasks");
        Subtask newSubtask = new Subtask("newSubtask", "Description",
                LocalDateTime.of(2022, 4, 2, 15, 10),
                Duration.ofMinutes(15), 4);
        String newSubtaskJson = gson.toJson(newSubtask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(
                HttpRequest.BodyPublishers.ofString(newSubtaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Subtask> subtaskFromManager = manager.getSubtasks();
        assertEquals(4, subtaskFromManager.size(), "Некорректное количество задач");
    }

    @Test
    void testUpdateSubtask() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/subtasks");
        Subtask subtask = manager.getSubtasks().getFirst();
        int epicId = subtask.getEpicId();
        Subtask updateSubtask = new Subtask("Subtask update", "Descr", subtask.getTaskId(),
                subtask.getStatus(), subtask.getStartTime(), subtask.getDuration(), epicId);
        String updatedEpicJson = gson.toJson(updateSubtask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(
                HttpRequest.BodyPublishers.ofString(updatedEpicJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        Task epicFromManager = manager.getSubtasks().getFirst();
        assertEquals(updateSubtask.getTaskName(), epicFromManager.getTaskName());
    }

    @Test
    void testDeleteSubtask() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/subtasks/6");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(2, manager.getSubtasks().size());
    }
}

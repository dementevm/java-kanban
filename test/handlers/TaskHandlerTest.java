package handlers;

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

public class TaskHandlerTest extends BaseHttpHandlerTest {

    @Test
    public void getTasks() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/tasks");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        List<Task> tasksFromResponse = gson.fromJson(response.body(), taskListType);
        assertEquals(manager.getTasks(), tasksFromResponse);
    }

    @Test
    public void getTask() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        Task taskFromResponse = gson.fromJson(response.body(), Task.class);
        assertEquals(manager.getTasks().getFirst(), taskFromResponse);
    }

    @Test
    public void testCreateTask() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/tasks");
        Task newTask = new Task("Task4", "Description",
                LocalDateTime.of(2027, 4, 2, 15, 10),
                Duration.ofMinutes(15));
        String newTaskJson = gson.toJson(newTask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(
                HttpRequest.BodyPublishers.ofString(newTaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        List<Task> tasksFromManager = manager.getTasks();
        assertEquals(4, tasksFromManager.size(), "Некорректное количество задач");
    }

    @Test
    void updateTask() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/tasks");
        Task updatedTask = new Task("Task1", "Description", 1,
                LocalDateTime.of(2025, 4, 2, 15, 10),
                Duration.ofMinutes(15));
        String updatedTaskJson = gson.toJson(updatedTask);
        HttpRequest request = HttpRequest.newBuilder().uri(url).POST(
                HttpRequest.BodyPublishers.ofString(updatedTaskJson)).build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(201, response.statusCode());
        Task taskFromManager = manager.getTasks().getFirst();
        assertEquals(updatedTask.getTaskName(), taskFromManager.getTaskName());
    }

    @Test
    void deleteTask() throws IOException, InterruptedException {
        URI url = URI.create(BASE_URL + "/tasks/1");
        HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
        HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        assertEquals(200, response.statusCode());
        assertEquals(2, manager.getTasks().size());
    }
}
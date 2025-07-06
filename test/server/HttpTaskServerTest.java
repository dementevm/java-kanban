package server;

import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import util.Managers;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;

class HttpTaskServerTest {

    private static HttpTaskServer server;
    private static final int PORT = 8080;
    private static final String BASE_URL = "http://localhost:" + PORT;
    private static final HttpClient client = HttpClient.newHttpClient();

    @BeforeAll
    static void startServer() throws IOException {
        server = new HttpTaskServer(Managers.getDefault());
        server.start();
    }

    @AfterAll
    static void stopServer() throws IOException {
        server.stop();
    }

    @Test
    void testContextPaths() throws IOException, InterruptedException {
        List<String> paths = List.of("/tasks", "/subtasks", "/epics", "/history", "/prioritized");
        for (String path : paths) {
            URI uri = URI.create(BASE_URL + path);
            HttpRequest request = HttpRequest.newBuilder(uri).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            assertEquals(200, response.statusCode(), "Недоступен URL по пути: " + path);
        }
    }
}
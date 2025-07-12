package handlers;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import controller.TaskManager;
import model.Epic;
import model.Subtask;
import model.Task;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import server.HttpTaskServer;
import util.DurationAdapter;
import util.LocalDateTimeAdapter;
import util.Managers;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.http.HttpClient;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.ArrayList;

abstract class BaseHttpHandlerTest {
    private static final int PORT = 8080;
    protected static final String BASE_URL = "http://localhost:" + PORT;
    protected static final HttpClient client = HttpClient.newHttpClient();
    protected static final Gson gson = new GsonBuilder()
            .registerTypeAdapter(LocalDateTime.class, new LocalDateTimeAdapter())
            .registerTypeAdapter(Duration.class, new DurationAdapter())
            .serializeNulls()
            .excludeFieldsWithoutExposeAnnotation()
            .create();
    protected final Type taskListType = new TypeToken<ArrayList<Task>>() {
    }.getType();
    protected final Type subtaskListType = new TypeToken<ArrayList<Subtask>>() {
    }.getType();
    protected final Type epicListType = new TypeToken<ArrayList<Epic>>() {
    }.getType();
    protected static HttpTaskServer taskServer;
    protected TaskManager manager = Managers.getDefault();

    @BeforeEach
    public void setUp() throws IOException {
        for (int i = 1; i < 4; i++) {
            Task task = new Task("Task" + i, "Description",
                    LocalDateTime.now().plusHours(i), Duration.ofMinutes(15));
            manager.createTask(task);
        }
        for (int i = 1; i < 3; i++) {
            Epic epic = new Epic("Epic" + i, "EpicDescription");
            manager.createEpic(epic);
        }
        for (int i = 1; i < 4; i++) {
            Subtask subtask = new Subtask("Subtask" + i, "SubtaskDescription",
                    LocalDateTime.of(2025, 7, 4, 10 + i, 10),
                    Duration.ofMinutes(15), 4);
            manager.createSubtask(subtask);
        }
        taskServer = new HttpTaskServer(manager);
        taskServer.start();
    }

    @AfterEach
    public void shutDown() throws IOException {
        manager.deleteTasks();
        manager.deleteEpics();
        manager.deleteSubtasks();
        taskServer.stop();
    }
}

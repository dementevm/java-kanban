package server;

import com.sun.net.httpserver.HttpServer;
import controller.TaskManager;
import http_handlers.*;
import util.Managers;

import java.io.IOException;
import java.net.InetSocketAddress;


public class HttpTaskServer {
    private static final int PORT = 8080;
    private final HttpServer server;
    private TaskManager manager;

    public HttpTaskServer(TaskManager manager) throws IOException {
        this.manager = manager;
        this.server = HttpServer.create(new InetSocketAddress(PORT), 0);
        this.server.createContext("/tasks", new TaskHandler(manager));
        this.server.createContext("/epics", new EpicHandler(manager));
        this.server.createContext("/subtasks", new SubtaskHandler(manager));
        this.server.createContext("/history", new HistoryHandler(manager));
        this.server.createContext("/prioritized", new PrioritizedHandler(manager));
    }

    public void start() {
        server.start();
    }

    public void stop() throws IOException {
        server.stop(0);
    }

    public static void main(String[] args) throws IOException {
        new HttpTaskServer(Managers.getDefault()).start();
    }
}

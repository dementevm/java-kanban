package exceptions;

public class TaskCreateError extends RuntimeException {
    public TaskCreateError() {
    }

    public TaskCreateError(String message) {
        super(message);
    }
}

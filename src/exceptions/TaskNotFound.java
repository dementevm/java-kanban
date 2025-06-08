package exceptions;

public class TaskNotFound extends Exception {
    public TaskNotFound() {
        super("Задача с таким ID - отсутствует");
    }

    public TaskNotFound(String message) {
        super(message);
    }
}

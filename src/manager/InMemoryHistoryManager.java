package manager;

import task.Task;

import java.util.ArrayList;

public class InMemoryHistoryManager implements HistoryManager {
    private static final int HISTORY_MAX_SIZE = 10;
    private final ArrayList<Task> history = new ArrayList<>();

    @Override
    public void add(Task task) {
        if (task == null) {
            return;
        }

        // Создаем копию задачи перед добавлением в историю
        Task taskCopy = copyTask(task);
        history.add(taskCopy);

        if (history.size() == HISTORY_MAX_SIZE) {
            history.remove(0);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }

    private Task copyTask(Task task) {
        if (task == null) {
            return null;
        }

        Task copy = new Task(task.getName(), task.getStatus(), task.getDescription());
        copy.setId(task.getId());
        return copy;
    }
}

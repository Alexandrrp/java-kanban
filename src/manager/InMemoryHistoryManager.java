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
        Task copy = task.copyTask();
        history.add(copy);

        if (history.size() > HISTORY_MAX_SIZE) {
            history.remove(0);
        }
    }

    @Override
    public ArrayList<Task> getHistory() {
        return new ArrayList<>(history);
    }

    @Override
    public void remove(int id) {

    }
}

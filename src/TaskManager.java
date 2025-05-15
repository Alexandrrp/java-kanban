import java.util.HashMap;
import java.util.Map;

public class TaskManager {
    private Map<Long, Task> tasks = new HashMap<>();
    private long nextId = 1L;

    public Task createSimpleTask(String name, String description) {
        Task simpleTask = new SimpleTask(nextId++, name, description);
        tasks.put(simpleTask.getId(), simpleTask);
        return simpleTask;
    }

    public Epic createEpic(String name, String description) {
        Epic epic = new Epic(nextId++, name, description);
        tasks.put(epic.getId(), epic);
        return epic;
    }

    public Subtask createSubtask(Epic epic, String name, String description) {
        Subtask subtask = new Subtask(nextId++, name, description, epic.getId());
        epic.addSubtask(subtask);
        tasks.put(subtask.getId(), subtask);
        return subtask;
    }

    public Task getTask(long id) {
        return tasks.get(id);
    }

    public void updateAllStatuses() {
        for (Task task : tasks.values()) {
            if (task instanceof Epic) {
                ((Epic) task).updateStatus(tasks.values());
            }
        }
    }

    @Override
    public String toString() {
        return "TaskManager{" +
                "tasks=" + tasks +
                '}';
    }
}
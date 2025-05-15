import java.util.*;

public class Epic extends Task {
    private final Set<Long> subtasksIds = new HashSet<>();

    public Epic(long id, String name, String description) {
        super(id, name, description);
    }

    public void addSubtask(Subtask subtask) {
        subtasksIds.add(subtask.getId());
    }

    public Collection<Long> getSubtasksIds() {
        return Collections.unmodifiableSet(subtasksIds);
    }

    /**
     * Определим автоматическое обновление статуса эпика
     */
    public void updateStatus(Collection<Task> allTasks) {
        List<Status> statuses = new ArrayList<>();
        for (long subtaskId : subtasksIds) {
            Optional<Task> maybeSubtask = findTask(allTasks, subtaskId);
            if (maybeSubtask.isPresent()) {
                Subtask subtask = (Subtask) maybeSubtask.get();
                statuses.add(subtask.getStatus());
            }
        }

        if (statuses.contains(Status.IN_PROGRESS)) {
            this.status = Status.IN_PROGRESS;
        } else if (statuses.stream().allMatch(status -> status == Status.DONE)) {
            this.status = Status.DONE;
        } else {
            this.status = Status.NEW;
        }
    }

    private Optional<Task> findTask(Collection<Task> tasks, long id) {
        return tasks.stream()
                .filter(task -> task.getId() == id)
                .findFirst();
    }

    @Override
    public String toString() {
        return "Epic{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", status=" + status +
                ", subtasksIds=" + subtasksIds +
                '}';
    }
}
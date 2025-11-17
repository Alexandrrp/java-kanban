package task;

import manager.TaskManager;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;
import java.util.ArrayList;

public class Epic extends Task {
    private ArrayList<Integer> subtasksId;
    private LocalDateTime endTime;
    private TaskManager taskManager;

    public Epic(String name, String description) {
        super(name, Status.NEW, description);
        subtasksId = new ArrayList<>();
        this.taskManager = manager.Managers.getDefault();
    }

    public ArrayList<Integer> getSubtasksIds() {
        return new ArrayList<>(subtasksId);
    }

    public void addSubtaskId(int subtaskId) {
        subtasksId.add(subtaskId);
    }

    public void removeSubtaskId(int subtaskId) {
        subtasksId.remove((Integer) subtaskId);
    }

    public void updateStatus(Status status) {
        super.internalSetStatus(status);
    }


    @Override
    public Duration getDuration() {
        if (subtasksId.isEmpty()) {
            return Duration.ZERO;
        }

        long totalMinutes = subtasksId.stream()
                .mapToLong(id -> {
                    Subtask subtask = (Subtask) getTaskById(id);
                    return subtask != null && subtask.getDuration() != null ?
                            subtask.getDuration().toMinutes() : 0;
                })
                .sum();

        return Duration.ofMinutes(totalMinutes);
    }

    @Override
    public LocalDateTime getStartTime() {
        if (subtasksId.isEmpty()) {
            return null;
        }

        return subtasksId.stream()
                .map(this::getTaskById)
                .filter(Objects::nonNull)
                .map(Task::getStartTime)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo)
                .orElse(null);
    }

    @Override
    public LocalDateTime getEndTime() {
        if (subtasksId.isEmpty()) {
            return null;
        }

        return subtasksId.stream()
                .map(this::getTaskById)
                .filter(Objects::nonNull)
                .map(Task::getEndTime)
                .filter(Objects::nonNull)
                .max(LocalDateTime::compareTo)
                .orElse(null);
    }

    private Task getTaskById(int id) {
        return taskManager.getSubtask(id);
    }

    public TaskType getType() {
        return TaskType.EPIC;
    }

    @Override
    public String toString() {
        return "task.Epic{" + "subtasksId=" + subtasksId + ", name='" + super.getName() + '\''
                + ", id=" + super.getId() +
                ", description='" + super.getDescription() + '\'' +
                ", status=" + super.getStatus() +
                ", startTime=" + getStartTime() +
                ", duration=" + getDuration() +
                ", endTime=" + getEndTime() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Epic epic = (Epic) o;
        return Objects.equals(subtasksId, epic.subtasksId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), subtasksId, endTime);
    }

    @Override
    public void setStatus(Status status) {
        throw new UnsupportedOperationException("Используйте updateStatus()");
    }
}
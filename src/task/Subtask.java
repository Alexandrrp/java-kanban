package task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.Objects;

public class Subtask extends Task {
    private int epicId;

    public Subtask(String name, Status status, String description, int epicId) {
        super(name, status, description);
        this.epicId = epicId;
    }

    public Subtask(String name, Status status, String description,
                   int epicId, LocalDateTime startTime, Duration duration) {
        super(name, status, description, startTime, duration);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    public TaskType getType() {
        return TaskType.SUBTASK;
    }


    @Override
    public String toString() {
        return "task.Subtask{" + "epicId=" + epicId + ", name='" + super.getName() + '\''
                + ", id=" + super.getId() +
                ", description='" + super.getDescription() + '\'' +
                ", status=" + super.getStatus() +
                ", startTime=" + super.getStartTime() +
                ", duration=" + super.getDuration() +
                ", endTime=" + super.getEndTime() + '}';
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        if (!super.equals(o)) return false;
        Subtask subtask = (Subtask) o;
        return epicId == subtask.epicId;
    }

    @Override
    public int hashCode() {
        return Objects.hash(super.hashCode(), epicId);
    }
}
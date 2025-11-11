package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskType;
import task.Status;

import java.time.Duration;
import java.time.LocalDateTime;

public class FileSerializer {

    public static String toString(Task task) {
        String type = task.getType().name();
        String name = task.getName();
        String status = task.getStatus().name();
        String description = task.getDescription();
        String startTime = task.getStartTime() != null ? task.getStartTime().toString() : "";
        String duration = task.getDuration() != null ? String.valueOf(task.getDuration().toMinutes()) : "";
        String epic = "";

        if (task.getType() == TaskType.SUBTASK) {
            epic = String.valueOf(((Subtask) task).getEpicId());
        }

        return String.format("%d,%s,%s,%s,%s,%s", task.getId(), type, name, status, description, startTime,
                duration, epic);
    }

    public static Task fromString(String value) {
        String[] elements = value.split(",");
        int id = Integer.parseInt(elements[0]);
        TaskType type = TaskType.valueOf(elements[1]);
        String name = elements[2];
        Status status = Status.valueOf(elements[3]);
        String description = elements[4];
        LocalDateTime startTime = elements[5].isEmpty() ? null : LocalDateTime.parse(elements[5]);
        Duration duration = elements[6].isEmpty() ? null : Duration.ofMinutes(Long.parseLong(elements[6]));

        switch (type) {
            case TASK -> {
                Task task = new Task(name, status, description, startTime, duration);
                task.setId(id);
                return task;
            }
            case EPIC -> {
                Epic epic = new Epic(name, description);
                epic.setId(id);
                return epic;
            }
            case SUBTASK -> {
                int epicId = elements[7].isEmpty() ? 0 : Integer.parseInt(elements[7]);
                Subtask subtask = new Subtask(name, status, description, epicId, startTime, duration);
                subtask.setId(id);
                return subtask;
            }
            default -> {
                return null;
            }
        }
    }
}
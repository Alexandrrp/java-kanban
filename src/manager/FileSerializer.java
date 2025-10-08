package manager;

import task.Epic;
import task.Subtask;
import task.Task;
import task.TaskType;
import task.Status;

public class FileSerializer {

    public static String toString(Task task) {
        String type = task.getType().name();
        String name = task.getName();
        String status = task.getStatus().name();
        String description = task.getDescription();
        String epic = "";

        if (task.getType() == TaskType.SUBTASK) {
            epic = String.valueOf(((Subtask) task).getEpicId());
        }

        return String.format("%d,%s,%s,%s,%s,%s", task.getId(), type, name, status, description, epic);
    }

    public static Task fromString(String value) {
        String[] elements = value.split(",");
        int id = Integer.parseInt(elements[0]);
        TaskType type = TaskType.valueOf(elements[1]);
        String name = elements[2];
        Status status = Status.valueOf(elements[3]);
        String description = elements[4];

        switch (type) {
            case TASK -> {
                Task task = new Task(name, status, description);
                task.setId(id);
                return task;
            }
            case EPIC -> {
                Epic epic = new Epic(name, description);
                epic.setId(id);
                return epic;
            }
            case SUBTASK -> {
                int epicId = elements[5].isEmpty() ? 0 : Integer.parseInt(elements[5]);
                Subtask subtask = new Subtask(name, status, description, epicId);
                subtask.setId(id);
                return subtask;
            }
            default -> {
                return null;
            }
        }
    }
}
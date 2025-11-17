import manager.*;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Task1", Status.NEW, "Description1",
                LocalDateTime.of(2025, 9, 3, 12, 0), Duration.ofMinutes(10));
        taskManager.createTask(task1);
        taskManager.getTask(task1.getId());
        Task task2 = new Task("Task2", Status.IN_PROGRESS, "Description2",
                LocalDateTime.of(2025, 9, 3, 13, 0), Duration.ofMinutes(30));
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Epic1", "Description1");
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1.1", Status.NEW, "SubTask Description1.1",
                epic1.getId(), LocalDateTime.of(2025, 9, 9, 9, 0),
                Duration.ofHours(5));
        taskManager.createSubtask(subtask1);
        taskManager.getSubtask(subtask1.getId());
        Subtask subtask2 = new Subtask("Sub task 1.2", Status.IN_PROGRESS, "SubTask Desc 1.2",
                epic1.getId(), LocalDateTime.of(2025, 9, 9, 16, 0),
                Duration.ofMinutes(180));
        taskManager.createSubtask(subtask2);

        Epic epic2 = new Epic("Epic2", "Desc1");
        taskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Sub task2.1", Status.IN_PROGRESS, "Desc2.1",
                epic2.getId(), LocalDateTime.of(2025, 9, 10, 10, 0),
                Duration.ofMinutes(40));
        taskManager.createSubtask(subtask3);

        // Тестирование новых методов
        System.out.println("Приоритетные задачи:");
        taskManager.getPrioritizedTasks().forEach(System.out::println);

        System.out.println("Расчет времени эпиков:");
        System.out.println("Старт: " + epic1.getStartTime());
        System.out.println("Продолжительность: " + epic1.getDuration());
        System.out.println("Конец: " + epic1.getEndTime());

        // Проверка пересечений
        try {
            Task overlappTask = new Task("Overlap", Status.NEW, "Desc",
                    LocalDateTime.of(2025, 9, 3, 13, 10), Duration.ofMinutes(30));
            taskManager.createTask(overlappTask); // ← Вызовет исключение при пересечении
            System.out.println("Задача создана успешно (пересечений нет)");
        } catch (IllegalStateException e) {
            System.out.println("Имеет пересечения: " + e.getMessage());
        }

        System.out.println("Все задачи:");
        System.out.println(taskManager.getAllTasks());
        System.out.println("Все эпики:");
        System.out.println(taskManager.getAllEpics());
        System.out.println("Все подзадачи:");
        System.out.println(taskManager.getAllSubtasks());
        System.out.println();
        System.out.println("-".repeat(50));
        System.out.println();

        subtask1.setStatus(Status.IN_PROGRESS);
        subtask3.setStatus(Status.DONE);
        taskManager.updateEpic(epic1);
        taskManager.updateEpic(epic2);

        System.out.println("Эпики после изменения статусов подзадач:");
        System.out.println(taskManager.getAllEpics());
        System.out.println();
        System.out.println("-".repeat(50));
        System.out.println();

        taskManager.deleteTask(task2.getId());
        taskManager.deleteSubtask(subtask1.getId());
        taskManager.deleteEpic(epic2.getId());

        System.out.println("Осталось задач:");
        System.out.println(taskManager.getAllTasks());
        System.out.println("Осталось эпиков:");
        System.out.println(taskManager.getAllEpics());
        System.out.println("Осталось подзадач:");
        System.out.println(taskManager.getAllSubtasks());

        printAllTasks(taskManager);
    }

    private static void printAllTasks(TaskManager manager) {
        System.out.println("Задачи:");
        for (Task task : manager.getAllTasks()) {
            System.out.println(task);
        }
        System.out.println("Эпики:");
        for (Task epic : manager.getAllEpics()) {
            System.out.println(epic);

            for (Task task : manager.getEpicSubtasks(epic.getId())) {
                System.out.println("--> " + task);
            }
        }
        System.out.println("Подзадачи:");
        for (Task subtask : manager.getAllSubtasks()) {
            System.out.println(subtask);
        }

        System.out.println("История:");
        for (Task task : manager.getHistory()) {
            System.out.println(task);
        }
    }
}
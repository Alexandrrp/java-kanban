package task;

import manager.*;

public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = Managers.getDefault();

        Task task1 = new Task("Task1", Status.NEW, "Description1");
        taskManager.createTask(task1);
        taskManager.getTask(task1.getId());
        Task task2 = new Task("Task2", Status.IN_PROGRESS, "Description2");
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Epic1", "Description1");
        taskManager.createEpic(epic1);
        taskManager.getEpic(epic1.getId());
        Subtask subtask1_1 = new Subtask("Subtask1.1", Status.NEW, "SubTask Description1.1",
                epic1.getId());
        taskManager.createSubtask(subtask1_1);
        taskManager.getSubtask(subtask1_1.getId());
        Subtask subtask1_2 = new Subtask("Sub task 1.2", Status.IN_PROGRESS, "SubTask Desc 1.2",
                epic1.getId());
        taskManager.createSubtask(subtask1_2);
        taskManager.getSubtask(subtask1_2.getId());

        Epic epic2 = new Epic("Epic2", "Desc1");
        taskManager.createEpic(epic2);
        Subtask subtask2_1 = new Subtask("Sub task2.1", Status.IN_PROGRESS, "Desc2.1",
                epic2.getId());
        taskManager.createSubtask(subtask2_1);

        System.out.println("Все задачи:");
        System.out.println(taskManager.getAllTasks());
        System.out.println("Все эпики:");
        System.out.println(taskManager.getAllEpics());
        System.out.println("Все подзадачи:");
        System.out.println(taskManager.getAllSubtasks());
        System.out.println();
        System.out.println("-".repeat(50));
        System.out.println();

        subtask1_1.setStatus(Status.IN_PROGRESS);
        subtask2_1.setStatus(Status.DONE);
        taskManager.updateEpic(epic1);
        taskManager.updateEpic(epic2);

        System.out.println("Эпики после изменения статусов подзадач:");
        System.out.println(taskManager.getAllEpics());
        System.out.println();
        System.out.println("-".repeat(50));
        System.out.println();

        taskManager.deleteTask(task2.getId());
        taskManager.deleteSubtask(subtask1_1.getId());
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
public class Main {
    public static void main(String[] args) {
        TaskManager manager = new TaskManager();

        // Создаём простую задачу
        Task simpleTask = manager.createSimpleTask("Купить молоко", "Куплю завтра утром");
        System.out.println("Task: " + simpleTask);

        // Создаём эпик
        Epic epic = manager.createEpic("Переезд", "Надо переехать в новый дом");
        System.out.println("Epic: " + epic);

        // Создаём две подзадачи
        Subtask subtask1 = manager.createSubtask(epic, "Упаковать вещи", "Закончить упаковку вещей");
        Subtask subtask2 = manager.createSubtask(epic, "Нанять грузчиков", "Позвонить друзьям");
        System.out.println("Subtask 1: " + subtask1);
        System.out.println("Subtask 2: " + subtask2);

        // Проверим, что эпик помечен как "NEW"
        System.out.println("Epic Status: " + epic.getStatus());

        // Ставим первую подзадачу в прогресс
        subtask1.setStatus(Status.IN_PROGRESS);
        manager.updateAllStatuses();
        System.out.println("Epic Status after one subtask in progress: " + epic.getStatus());

        // Завершаем вторую подзадачу
        subtask2.setStatus(Status.DONE);
        manager.updateAllStatuses();
        System.out.println("Epic Status after second subtask done: " + epic.getStatus());

        // Завершена первая подзадача
        subtask1.setStatus(Status.DONE);
        manager.updateAllStatuses();
        System.out.println("Epic Status after both subtasks are done: " + epic.getStatus());
    }
}
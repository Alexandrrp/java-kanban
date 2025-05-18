public class Main {

    public static void main(String[] args) {
        TaskManager taskManager = new TaskManager();

        Task task1 = new Task("Task1", Status.NEW, "Description1");
        taskManager.createTask(task1);
        Task task2 = new Task("Task2", Status.IN_PROGRESS, "Description2");
        taskManager.createTask(task2);

        Epic epic1 = new Epic("Epic1", "Description1");
        taskManager.createEpic(epic1);
        Subtask subtask1 = new Subtask("Subtask1.1", Status.NEW, "SubTask Description1.1",
                epic1.getId());
        taskManager.createSubtask(subtask1);
        Subtask subtask2 = new Subtask("Subtask1.2", Status.DONE, "SubTask Description1.2",
                epic1.getId());
        taskManager.createSubtask(subtask2);

        Epic epic2 = new Epic("Epic1", "Description1");
        taskManager.createEpic(epic2);
        Subtask subtask3 = new Subtask("Subtask2.1", Status.DONE, "Description2.1",
                epic2.getId());
        taskManager.createSubtask(subtask3);

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println();

        subtask1.setStatus(Status.DONE);
        subtask3.setStatus(Status.IN_PROGRESS);
        taskManager.updateEpic(epic1);
        taskManager.updateEpic(epic2);

        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
        System.out.println();

        taskManager.deleteTask(task2.getId());
        taskManager.deleteSubtask(subtask1.getId());
        taskManager.deleteEpic(epic2.getId());

        System.out.println(taskManager.getAllTasks());
        System.out.println(taskManager.getAllEpics());
        System.out.println(taskManager.getAllSubtasks());
    }
}
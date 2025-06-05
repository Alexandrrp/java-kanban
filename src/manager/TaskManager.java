package manager;

import task.Epic;
import task.Subtask;
import task.Task;

import java.util.ArrayList;

public interface TaskManager {
    ArrayList<Task> getHistory();

    //методы для класса task.Task
    ArrayList<Task> getAllTasks();

    void deleteAllTasks();

    void deleteTask(int id);

    Task getTask(int id);

    Task createTask(Task task);

    void updateTask(Task task);

    //методы для класса task.Epic
    ArrayList<Epic> getAllEpics();

    void deleteAllEpics();

    void deleteEpic(int id);

    Epic getEpic(int id);

    Epic createEpic(Epic epic);

    void updateEpic(Epic epic);

    ArrayList<Subtask> getEpicSubtasks(int epicId);

    //методы для класса task.Subtask
    ArrayList<Subtask> getAllSubtasks();

    void deleteAllSubtasks();

    Subtask getSubtask(int id);

    Subtask createSubtask(Subtask subtask);

    void updateSubtask(Subtask subtask);

    void deleteSubtask(int id);
}

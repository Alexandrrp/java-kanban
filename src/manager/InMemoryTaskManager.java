package manager;

import java.util.HashMap;
import java.util.ArrayList;

import task.Epic;
import task.Subtask;
import task.Task;
import task.Status;

public class InMemoryTaskManager implements TaskManager {
    private final HashMap<Integer, Task> tasks = new HashMap<>();
    private final HashMap<Integer, Epic> epics = new HashMap<>();
    private final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final HistoryManager historyManager = Managers.getDefaultHistory();
    private int nextId = 1;

    @Override
    public ArrayList<Task> getHistory() {
        return historyManager.getHistory();
    }

    //методы для класса task.Task
    @Override
    public ArrayList<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public void deleteAllTasks() {
        tasks.clear();
    }

    @Override
    public void deleteTask(int id) {
        tasks.remove(id);
    }

    @Override
    public Task getTask(int id) {

        Task task = tasks.get(id);
        if (task != null) {
            historyManager.add(task);
        }
        return task;
    }

    @Override
    public Task createTask(Task task) {
        // Проверяем, задан ли ID вручную
        if (task.getId() != 0) {
            // Если задача с таким ID уже существует — бросаем исключение
            if (tasks.containsKey(task.getId())) {
                throw new IllegalArgumentException("Задача с ID = " + task.getId() + " уже существует!");
            }
            // Обновляем nextId, если ручной ID больше текущего
            if (task.getId() >= nextId) {
                nextId = task.getId() + 1;
            }
        } else {
            // Автогенерация ID
            task.setId(nextId++);
        }
        tasks.put(task.getId(), task);
        return task;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            tasks.put(task.getId(), task);
        }
    }

    //методы для класса task.Epic
    @Override
    public ArrayList<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            for (int subtaskId : epic.getSubtasksIds()) {
                subtasks.remove(subtaskId);
            }
        }
    }

    @Override
    public Epic getEpic(int id) {

        Epic epic = epics.get(id);
        if (epic != null) {
            historyManager.add(epic);
        }
        return epic;
    }

    @Override
    public Epic createEpic(Epic epic) {
        epic.setId(nextId++);
        epics.put(epic.getId(), epic);
        return epic;
    }

    @Override
    public void updateEpic(Epic epic) {
        if (epics.containsKey(epic.getId())) {
            Epic updatedEpic = epics.get(epic.getId());
            updatedEpic.setName(epic.getName());
            updatedEpic.setDescription(epic.getDescription());
        }
        updateEpicStatus(epic.getId());
    }

    @Override
    public ArrayList<Subtask> getEpicSubtasks(int epicId) {
        ArrayList<Subtask> result = new ArrayList<>();
        Epic epic = epics.get(epicId);
        if (epic != null) {
            for (int subtaskId : epic.getSubtasksIds()) {
                result.add(subtasks.get(subtaskId));
            }
        }
        return result;
    }

    private void updateEpicStatus(int epicId) {
        boolean allDone = true;
        boolean allNew = true;
        Epic epic = epics.get(epicId);

        if (epic == null) {
            return;
        }
        if (epic.getSubtasksIds().isEmpty()) {
            epic.updateStatus(Status.NEW);
            return;
        }

        ArrayList<Integer> invalidIds = new ArrayList<>();

        for (int subtaskId : epic.getSubtasksIds()) {
            Subtask subtask = subtasks.get(subtaskId);
            if (subtask == null) {
                invalidIds.add(subtaskId);
                continue;
            }
            if (subtask.getStatus() != Status.DONE) {
                allDone = false;
            }
            if (subtask.getStatus() != Status.NEW) {
                allNew = false;
            }
        }

        // После завершения обхода удаляем ненужные идентификаторы
        for (int invalidId : invalidIds) {
            epic.removeSubtaskId(invalidId);
        }

        if (allDone) {
            epic.updateStatus(Status.DONE);
        } else if (allNew) {
            epic.updateStatus(Status.NEW);
        } else {
            epic.updateStatus(Status.IN_PROGRESS);
        }
    }


    //методы для класса task.Subtask
    @Override
    public ArrayList<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.clear();
        for (Epic epic : epics.values()) {
            epic.getSubtasksIds().clear();
            updateEpicStatus(epic.getId());
        }
    }

    @Override
    public Subtask getSubtask(int id) {

        Subtask subtask = subtasks.get(id);
        if (subtask != null) {
            historyManager.add(subtask);
        }
        return subtask;
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        subtask.setId(nextId++);
        subtasks.put(subtask.getId(), subtask);
        Epic epic = epics.get(subtask.getEpicId());
        if (epic != null) {
            epic.addSubtaskId(subtask.getId());
            updateEpicStatus(epic.getId());
        }
        return subtask;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            subtasks.put(subtask.getId(), subtask);
            updateEpicStatus(subtask.getEpicId());
        }
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(id);
                updateEpicStatus(epic.getId());
            }
        }
    }
}
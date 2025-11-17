package manager;

import java.util.*;
import java.util.stream.Collectors;

import task.Epic;
import task.Subtask;
import task.Task;
import task.Status;

public class InMemoryTaskManager implements TaskManager {
    protected final HashMap<Integer, Task> tasks = new HashMap<>();
    protected final HashMap<Integer, Epic> epics = new HashMap<>();
    protected final HashMap<Integer, Subtask> subtasks = new HashMap<>();
    protected final HistoryManager historyManager = Managers.getDefaultHistory();
    protected int nextId = 1;
    private final Set<Task> prioritizedTasks = new TreeSet<>(
            Comparator.comparing(Task::getStartTime,
                    Comparator.nullsLast(Comparator.naturalOrder()))
    );

    @Override
    public List<Task> getHistory() {
        return historyManager.getHistory();
    }

    //методы для класса task.Task
    @Override
    public List<Task> getAllTasks() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public List<Task> getPrioritizedTasks() {
        return new ArrayList<>(prioritizedTasks);
    }

    public boolean hasTimeOverlap(Task task) {
        if (task.getStartTime() == null || task.getEndTime() == null) {
            return false;
        }

        return prioritizedTasks.stream()
                .filter(t -> t.getStartTime() != null && t.getEndTime() != null)
                .anyMatch(existingTask -> isTimeOverlapping(task, existingTask));
    }

    private boolean isTimeOverlapping(Task firstTask, Task secondTask) {
        return firstTask.getStartTime().isBefore(secondTask.getEndTime()) &&
                firstTask.getEndTime().isAfter(secondTask.getStartTime());
    }

    private void addToPrioritizedTasks(Task task) {
        prioritizedTasks.add(task);
    }

    private void removeFromPrioritizedTasks(Task task) {
        prioritizedTasks.remove(task);
    }

    @Override
    public void deleteAllTasks() {
        tasks.values().forEach(this::removeFromPrioritizedTasks);
        tasks.clear();
    }

    @Override
    public void deleteTask(int id) {
        Task task = tasks.remove(id);
        if (task != null) {
            historyManager.remove(id);
            removeFromPrioritizedTasks(task);
        }
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

        if (hasTimeOverlap(task)) {
            throw new IllegalStateException("Время выполнения задачи совпадает с существующим заданием");
        }

        tasks.put(task.getId(), task);
        addToPrioritizedTasks(task);
        return task;
    }

    @Override
    public void updateTask(Task task) {
        if (tasks.containsKey(task.getId())) {
            Task oldTask = tasks.get(task.getId());
            removeFromPrioritizedTasks(oldTask);

            if (hasTimeOverlap(task)) {
                // Вернет старую задачу, если новое время указано неверно
                addToPrioritizedTasks(oldTask);
                throw new IllegalStateException("Время выполнения обновленной задачи совпадает с временем " +
                        "выполнения существующей задачи");
            }
            tasks.put(task.getId(), task);
            addToPrioritizedTasks(task);
        }
    }

    //методы для класса task.Epic
    @Override
    public List<Epic> getAllEpics() {
        return new ArrayList<>(epics.values());
    }

    @Override
    public void deleteAllEpics() {
        epics.values().forEach(epic -> {
            historyManager.remove(epic.getId());
            epic.getSubtasksIds().forEach(subtaskId -> {
                historyManager.remove(subtaskId);
                removeFromPrioritizedTasks(subtasks.get(subtaskId));
            });
        });
        epics.clear();
        subtasks.clear();
    }

    @Override
    public void deleteEpic(int id) {
        Epic epic = epics.remove(id);
        if (epic != null) {
            historyManager.remove(id);
            epic.getSubtasksIds().forEach(subtaskId -> {
                historyManager.remove(subtaskId);
                removeFromPrioritizedTasks(subtasks.remove(subtaskId));
            });
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
        if (epic == null) {
            throw new IllegalArgumentException("Значение Epic не может быть пустым");
        }

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
    public List<Subtask> getEpicSubtasks(int epicId) {
        Epic epic = epics.get(epicId);
        if (epic == null) {
            return Collections.emptyList();
        }
        return epic.getSubtasksIds().stream()
                .map(subtasks::get)
                .filter(Objects::nonNull)
                .collect(Collectors.toList());
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
    public List<Subtask> getAllSubtasks() {
        return new ArrayList<>(subtasks.values());
    }

    @Override
    public void deleteAllSubtasks() {
        subtasks.values().forEach(this::removeFromPrioritizedTasks);
        subtasks.clear();
        epics.values().forEach(epic -> {
            epic.getSubtasksIds().clear();
            updateEpicStatus(epic.getId());
        });
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
        addToPrioritizedTasks(subtask);

        Epic epic = epics.get(subtask.getEpicId());
        epic.addSubtaskId(subtask.getId());
        updateEpicStatus(epic.getId());

        return subtask;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        if (subtasks.containsKey(subtask.getId())) {
            Subtask oldSubtask = subtasks.get(subtask.getId());
            removeFromPrioritizedTasks(oldSubtask);

            if (hasTimeOverlap(subtask)) {
                // Вернет старую подзадачу, если новое время указано неверно
                addToPrioritizedTasks(oldSubtask);
                throw new IllegalStateException("Время выполнения обновленной подзадачи совпадает с временем " +
                        "выполнения существующей задачи");
            }
            subtasks.put(subtask.getId(), subtask);
            addToPrioritizedTasks(subtask);
            updateEpicStatus(subtask.getEpicId());
        }
    }

    @Override
    public void deleteSubtask(int id) {
        Subtask subtask = subtasks.remove(id);
        if (subtask != null) {
            historyManager.remove(id);
            removeFromPrioritizedTasks(subtask);
            Epic epic = epics.get(subtask.getEpicId());
            if (epic != null) {
                epic.removeSubtaskId(id);
                updateEpicStatus(epic.getId());
            }
        }
    }
}
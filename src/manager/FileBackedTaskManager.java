package manager;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;

import task.Epic;
import task.Subtask;
import task.Task;

public class FileBackedTaskManager extends InMemoryTaskManager {
    private final File file;
    // Флаг для отслеживания процесса загрузки из файла
    private boolean isLoading = false;


    public FileBackedTaskManager(File file) {
        this.file = file;
    }

    public static FileBackedTaskManager loadFromFile(File file) {
        FileBackedTaskManager manager = new FileBackedTaskManager(file);
            // Устанавливаем флаг загрузки, чтобы предотвратить сохранение во время загрузки
        manager.isLoading = true;

        try {
            String content = Files.readString(file.toPath());
            String[] lines = content.split("\n");

            int maxId = 0; // Для отслеживания максимального ID из файла
            // Первый проход: создаем все задачи и находим максимальный ID
            for (int i = 1; i < lines.length; i++) {
                String line = lines[i].trim();
                if (!line.isEmpty()) {
                    Task task = FileSerializer.fromString(line);
                    if (task != null) {
                        int taskId = task.getId();
                        // Обновляем максимальный ID
                        if (taskId > maxId) {
                            maxId = taskId;
                        }

                            // Добавляем напрямую в хешмапы родительского класса (область видимости protected)
                            // Это предотвращает вызов методов создания, которые триггерят сохранение
                            switch (task.getType()) {
                                case TASK -> manager.tasks.put(taskId, task);
                                case EPIC -> manager.epics.put(taskId, (Epic) task);
                                case SUBTASK -> manager.subtasks.put(taskId, (Subtask) task);
                                default -> throw new ManagerSaveException("Неизвестный тип задачи");
                            }
                        }
                    }
                }

                // Синхронизируем счетчик ID: устанавливаем nextId на 1 больше максимального найденного ID
                if (maxId >= manager.nextId) {
                    manager.nextId = maxId + 1;
                }

                // Второй проход: восстанавливаем связи между подзадачами и эпиками
                for (Subtask subtask : manager.subtasks.values()) {
                    Epic epic = manager.epics.get(subtask.getEpicId());
                    if (epic != null) {
                        // Добавляем ID подзадачи в список подзадач эпика
                        epic.addSubtaskId(subtask.getId());
                    }
                }

                // Обновляем статусы всех эпиков после восстановления связей
                for (Epic epic : manager.epics.values()) {
                    manager.updateEpic(epic);
                }

            } catch (IOException e) {
                throw new ManagerSaveException("Ошибка загрузки из файла");
            } finally {
                manager.isLoading = false;
            }
            return manager;
        }

    private void save() {
        try {
            StringBuilder builder = new StringBuilder();
            builder.append("id,type,name,status,description,epic\n");

            for (Task task : getAllTasks()) {
                builder.append(FileSerializer.toString(task)).append("\n");
            }

            for (Epic epic : getAllEpics()) {
                builder.append(FileSerializer.toString(epic)).append("\n");
            }

            for (Subtask subtask : getAllSubtasks()) {
                builder.append(FileSerializer.toString(subtask)).append("\n");
            }

            Files.writeString(file.toPath(), builder.toString());
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения в файл");
        }
    }

    @Override
    public Task createTask(Task task) {
        Task createdTask = super.createTask(task);
        save();
        return createdTask;
    }

    @Override
    public void updateTask(Task task) {
        super.updateTask(task);
        save();
    }

    @Override
    public void deleteTask(int id) {
        super.deleteTask(id);
        save();
    }

    @Override
    public void deleteAllTasks() {
        super.deleteAllTasks();
        save();
    }

    @Override
    public Epic createEpic(Epic epic) {
        Epic createdEpic = super.createEpic(epic);
        save();
        return createdEpic;
    }

    @Override
    public void updateEpic(Epic epic) {
        super.updateEpic(epic);
        save();
    }

    @Override
    public void deleteEpic(int id) {
        super.deleteEpic(id);
        save();
    }

    @Override
    public void deleteAllEpics() {
        super.deleteAllEpics();
        save();
    }

    @Override
    public Subtask createSubtask(Subtask subtask) {
        Subtask createdSubtask = super.createSubtask(subtask);
        save();
        return createdSubtask;
    }

    @Override
    public void updateSubtask(Subtask subtask) {
        super.updateSubtask(subtask);
        save();
    }

    @Override
    public void deleteSubtask(int id) {
        super.deleteSubtask(id);
        save();
    }

    @Override
    public void deleteAllSubtasks() {
        super.deleteAllSubtasks();
        save();
    }
}

package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Epic;
import task.Status;
import task.Subtask;
import task.Task;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.time.Duration;
import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTaskManagerTest extends TaskManagerTest<FileBackedTaskManager> {
    private File tempFile;
    @Override
    protected FileBackedTaskManager createTaskManager() {
        try {
            tempFile = File.createTempFile("tasks", ".csv");
            return new FileBackedTaskManager(tempFile);
        } catch (IOException e) {
            throw new RuntimeException("Не удалось создать временный файл", e);
        }
    }

    @BeforeEach
    void clearFile() throws IOException {
        if (tempFile != null && tempFile.exists()) {
            Files.write(tempFile.toPath(), new byte[0]);
        }
    }

    @Test
    void shouldSaveAndLoadEmptyManager() {
        FileBackedTaskManager loaded = FileBackedTaskManager.loadFromFile(tempFile);

        assertTrue(loaded.getAllTasks().isEmpty());
        assertTrue(loaded.getAllEpics().isEmpty());
        assertTrue(loaded.getAllSubtasks().isEmpty());
    }

    @Test
    void shouldHandleFileReadError() {
        File invalidFile = new File("java-kanban/tasks.csv");
        assertThrows(ManagerSaveException.class, () ->
                FileBackedTaskManager.loadFromFile(invalidFile));
    }

    @Test
    void shouldHandleFileWriteError() {
        File readOnlyFile = new File("readonly.csv");
        readOnlyFile.setReadOnly();

        assertThrows(ManagerSaveException.class, () -> {
            FileBackedTaskManager manager = new FileBackedTaskManager(readOnlyFile);
            manager.createTask(new Task("Test", Status.NEW, "Desc"));
        });
    }
}

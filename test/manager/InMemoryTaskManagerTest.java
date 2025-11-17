package manager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import task.Epic;
import task.Subtask;
import task.Task;
import task.Status;

import java.time.Duration;
import java.time.LocalDateTime;

class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {
    @Override
    protected InMemoryTaskManager createTaskManager() {
        return new InMemoryTaskManager();
    }

    @Test
    void shouldNotAllowTaskToOverlapTasks() {
        Task task1 = new Task("Task 1", Status.NEW, "Desc 1",
                LocalDateTime.of(2025, 9, 1, 10, 0), Duration.ofMinutes(30));
        getTaskManager().createTask(task1);

        Task overlappingTask = new Task("Task 2", Status.NEW, "Desc 2",
                LocalDateTime.of(2025, 9, 1, 10, 15), Duration.ofMinutes(30));
        assertThrows(IllegalStateException.class, () -> getTaskManager().createTask(overlappingTask));
    }

    @Test
    void shouldCorrectCalculateEpicTime() {
        Epic epic = new Epic("Test Epic", "Desc Epic");

        Subtask subtask = new Subtask("Sub", Status.NEW, "Desc", epic.getId(),
                LocalDateTime.of(2023, 9, 1, 10, 0), Duration.ofMinutes(30));
        epic.addSubtaskId(subtask.getId());
        assertEquals(LocalDateTime.of(2023, 9, 1, 10, 0), subtask.getStartTime());
    }

    @Test
    void shouldHandleEpicWithNoSubtasksTime() {
        Epic epic = new Epic("Epic", "Desc Epic");
        getTaskManager().createEpic(epic);

        assertNull(epic.getStartTime());
        assertEquals(Duration.ZERO, epic.getDuration());
        assertNull(epic.getEndTime());
    }
}
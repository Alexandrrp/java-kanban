package task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import task.Status;

import java.time.Duration;
import java.time.LocalDateTime;

class TaskTest {

    @Test
    void shouldCorrectlyCalculateEndTime() {
        Task task = new Task("Task", Status.NEW, "Desc",
                LocalDateTime.of(2025, 9, 1, 9, 0), Duration.ofHours(2));

        assertEquals(LocalDateTime.of(2025, 9, 1, 11, 0), task.getEndTime());
    }

    @Test
    void shouldReturnNullEndTimeWhenNoStartTime() {
        Task task = new Task("Task", Status.NEW, "Desc");
        assertNull(task.getEndTime());
    }

    @Test
    void tasksWithSameIdShouldBeEqual() {
        Task task1 = new Task("Task 1", Status.NEW, "Description 1");
        task1.setId(1);
        Task task2 = new Task("Task 2", Status.DONE, "Description 2");
        task2.setId(1);

        assertEquals(task1, task2);
    }

    @Test
    public void tasksWithDifferentIdShouldNotBeEqual() {
        Task task1 = new Task("Task 1", Status.NEW,"Description 1");
        task1.setId(1);
        Task task2 = new Task("Task 1", Status.NEW,"Description 1");
        task2.setId(2);

        assertNotEquals(task1, task2, "Задачи с разными идентификаторами не должны быть одинаковыми");
    }

    @Test
    void shouldHandleTimeFieldChanges() {
        Task task = new Task("Task", Status.NEW, "Desc");
        assertNull(task.getStartTime());

        LocalDateTime time = LocalDateTime.now();
        task.setStartTime(time);
        assertEquals(time, task.getStartTime());

        Duration duration = Duration.ofMinutes(30);
        task.setDuration(duration);
        assertEquals(duration, task.getDuration());
    }
}

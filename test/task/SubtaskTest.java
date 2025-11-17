package task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import task.Status;

import java.time.Duration;
import java.time.LocalDateTime;


class SubtaskTest {

    @Test
    void shouldCorrectlyCalculateEndTime() {
        Subtask subtask = new Subtask("Sub", Status.NEW, "Desc", 1,
                LocalDateTime.of(2025, 9, 1, 10, 0), Duration.ofMinutes(45));

        assertEquals(LocalDateTime.of(2025, 9, 1, 10, 45), subtask.getEndTime());
    }

    @Test
    void shouldReturnNullEndTimeWhenNoStartTime() {
        Subtask subtask = new Subtask("Sub", Status.NEW, "Desc", 1);
        assertNull(subtask.getEndTime());
    }

    @Test
    void subtasksWithSameIdShouldBeEqual() {
        Subtask sub1 = new Subtask("Sub1", Status.NEW, "Desc", 2);
        sub1.setId(1);

        Subtask sub2 = new Subtask("Sub2", Status.DONE, "Desc2", 2);
        sub2.setId(1);

        assertEquals(sub1, sub2);

    }

    @Test
    public void subtasksWithDifferentIdShouldNotBeEqual() {
        Subtask subtask1 = new Subtask("Subtask 1", Status.NEW,"Description 1", 5);
        subtask1.setId(1);
        Subtask subtask2 = new Subtask("Subtask 1", Status.NEW,"Description 1", 5);
        subtask1.setId(2);

        assertNotEquals(subtask1, subtask2, "Задачи с разными идентификаторами не должны быть одинаковыми");
    }

    @Test
    void shouldMaintainEpicReference() {
        Subtask subtask = new Subtask("Sub", Status.NEW, "Desc", 5);
        assertEquals(5, subtask.getEpicId());

        subtask.setEpicId(10);
        assertEquals(10, subtask.getEpicId());
    }
}
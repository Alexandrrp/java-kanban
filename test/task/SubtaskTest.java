package task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import task.Status;


class SubtaskTest {
    @Test
    void subtasksWithSameIdShouldBeEqual() {
        Subtask subtask1 = new Subtask("Subtask 1", Status.NEW,"Description 1", 5);
        subtask1.setId(1);
        Subtask subtask2 = new Subtask("Subtask 2", Status.DONE,"Description 2", 5);
        subtask2.setId(1);

        assertEquals(subtask1, subtask2, "Задачи с одинаковым идентификатором должны быть одинаковыми");
        assertEquals(subtask1.hashCode(), subtask2.hashCode(), "Хэш-код задач с одинаковым " +
                "идентификатором должен быть одинаковым");
    }

    @Test
    public void subtasksWithDifferentIdShouldNotBeEqual() {
        Subtask subtask1 = new Subtask("Subtask 1", Status.NEW,"Description 1", 5);
        subtask1.setId(1);
        Subtask subtask2 = new Subtask("Subtask 1", Status.NEW,"Description 1", 5);
        subtask1.setId(2);

        assertNotEquals(subtask1, subtask2, "Задачи с разными идентификаторами не должны быть одинаковыми");
    }
}
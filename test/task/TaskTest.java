package task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;
import task.Status;

class TaskTest {
    @Test
    void tasksWithSameIdShouldBeEqual() {
        Task task1 = new Task("Task 1", Status.NEW, "Description 1");
        task1.setId(1);
        Task task2 = new Task("Task 2", Status.DONE, "Description 2");
        task2.setId(1);

        assertEquals(task1, task2, "Задачи с одинаковым идентификатором должны быть одинаковыми");
        assertEquals(task1.hashCode(), task2.hashCode(), "Хэш-код задач с одинаковым " +
                "идентификатором должен быть одинаковым");
    }

    @Test
    public void tasksWithDifferentIdShouldNotBeEqual() {
        Task task1 = new Task("Task 1", Status.NEW,"Description 1");
        task1.setId(1);
        Task task2 = new Task("Task 1", Status.NEW,"Description 1");
        task2.setId(2);

        assertNotEquals(task1, task2, "Задачи с разными идентификаторами не должны быть одинаковыми");
    }
}

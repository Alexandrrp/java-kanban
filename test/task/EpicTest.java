package task;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;


class EpicTest {
    @Test
    void epicsWithSameIdShouldBeEqual() {
        Epic epic1 = new Epic("Epic 1", "Description 1");
        epic1.setId(1);
        epic1.addSubtaskId(2);
        Epic epic2 = new Epic("Epic 2", "Description 2");
        epic2.setId(1);
        epic2.addSubtaskId(2);

        assertEquals(epic1, epic2, "Задачи с одинаковым идентификатором должны быть одинаковыми");
        assertEquals(epic1.hashCode(), epic2.hashCode(), "Хэш-код задач с одинаковым " +
                "идентификатором должен быть одинаковым");
    }

    @Test
    public void epicsWithDifferentIdShouldNotBeEqual() {
        Epic epic1 = new Epic("Epic 1", "Description 1");
        epic1.setId(1);
        epic1.addSubtaskId(12);
        Epic epic2 = new Epic("Epic 1", "Description 1");
        epic2.setId(2);
        epic2.addSubtaskId(24);

        assertNotEquals(epic1, epic2, "Задачи с разными идентификаторами не должны быть одинаковыми");
    }
}
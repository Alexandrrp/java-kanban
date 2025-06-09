package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Status;
import task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager;

    @Test
    void ShouldSaveAllIssueVersionsInHistory() {
        historyManager = Managers.getDefaultHistory();
        Task task = new Task("Task", Status.NEW, "Description");
        task.setId(1);

        historyManager.add(task);

        task.setName("Updated name");
        task.setStatus(Status.IN_PROGRESS);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size()); // Проверяем, что в истории 2 элемента
        assertEquals("Task", history.get(0).getName()); // Первая версия (старое имя)
        assertEquals(Status.NEW, history.get(0).getStatus()); // Первая версия (старый статус)
        assertEquals("Updated name", history.get(1).getName()); // Вторая версия (новое имя)
        assertEquals(Status.IN_PROGRESS, history.get(1).getStatus()); // Вторая версия (новый статус)
    }


}
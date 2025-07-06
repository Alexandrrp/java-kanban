package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Status;
import task.Task;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    Task task;
    @BeforeEach
    void setUp() {
        historyManager = Managers.getDefaultHistory();
        task = new Task("Task", Status.NEW, "Description");
        task.setId(1);
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        historyManager.add(task);
        historyManager.remove(task.getId());
        assertTrue(historyManager.getHistory().isEmpty());
    }

    @Test
    void shouldAddTasksToHistory() {
        Task task1 = new Task("Task1", Status.DONE, "Description1");
        task1.setId(2);
        Task task2 = new Task("Task2", Status.IN_PROGRESS, "Description2");
        task2.setId(3);
        historyManager.add(task);
        historyManager.add(task1);
        historyManager.add(task2);

        assertEquals(List.of(task, task1, task2), historyManager.getHistory());
    }

    @Test
    void shouldNotAddDuplicatesTasks() {
        historyManager.add(task);
        historyManager.add(task);
        // проверка на дубликаты
        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void shouldNotAddAlreadyExistingTasksToHistory() {
        Task task2 = new Task("Task 2", Status.IN_PROGRESS, "Description2");
        task2.setId(2);

        historyManager.add(task);
        historyManager.add(task2);
        historyManager.add(task);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task, history.get(1));
    }
    @Test
    void shouldNoLimitHistorySize() {
        // Добавляем 15 задач
        for (int i = 1; i <= 15; i++) {
            Task task = new Task("Task" + i, Status.NEW, "Description" + i);
            task.setId(i);
            historyManager.add(task);
        }

        List<Task> history = historyManager.getHistory();
        // проверяем, что теперь больше 10 задач (должно быть 15)
        assertEquals(15, history.size());
        // проверяем, что первая задача в истории теперь с id=1
        assertEquals(1, history.get(0).getId());
        // проверяем, что теперь последняя задача с id=15
        assertEquals(15, history.get(history.size() - 1).getId());
    }
}
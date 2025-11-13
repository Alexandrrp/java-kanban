package manager;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import task.Status;
import task.Task;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class InMemoryHistoryManagerTest {
    HistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Task task3;

    @BeforeEach
    void setUp() {
        historyManager = new InMemoryHistoryManager();
        task1 = new Task("Task 1", Status.NEW, "Desc 1");
        task1.setId(1);
        task2 = new Task("Task 2", Status.IN_PROGRESS, "Desc 2");
        task2.setId(2);
        task3 = new Task("Task 3", Status.DONE, "Desc 3",
                LocalDateTime.now(), Duration.ofMinutes(30));
        task3.setId(3);
    }

    @Test
    void shouldRemoveTaskFromHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.remove(task1.getId());

        List<Task> history = historyManager.getHistory();
        assertEquals(1, history.size());
        assertEquals(task2, history.get(0));
    }

    @Test
    void shouldAddTasksToHistory() {
        historyManager.add(task1);
        historyManager.add(task2);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task1, history.get(0));
        assertEquals(task2, history.get(1));
    }

    @Test
    void shouldNotAddDuplicatesTasks() {
        historyManager.add(task1);
        historyManager.add(task1);
        // проверка на дубликаты
        assertEquals(1, historyManager.getHistory().size());
    }

    @Test
    void shouldNotAddAlreadyExistingTasksToHistory() {
        historyManager.add(task1);
        historyManager.add(task2);
        historyManager.add(task1);

        List<Task> history = historyManager.getHistory();
        assertEquals(2, history.size());
        assertEquals(task2, history.get(0));
        assertEquals(task1, history.get(1));
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
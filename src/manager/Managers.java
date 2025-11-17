package manager;

import java.io.File;

public class Managers {

    private static final File DEFAULT_FILE = new File("tasks.csv");

    private Managers() {
    }

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }

    public static TaskManager getFileBackedManager() {
        return new FileBackedTaskManager(DEFAULT_FILE);
    }
}
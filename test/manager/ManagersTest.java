package manager;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import manager.Managers;
import org.junit.jupiter.api.io.TempDir;

import java.io.File;

class ManagersTest {
    @Test
    void managerShouldBeInitialized() {
        assertNotNull(Managers.getDefault());
    }

    @Test
    void historyManagerShouldBeInitialized() {
        assertNotNull(Managers.getDefaultHistory());
    }

    @Test
    void fileBackedManagerShouldBeInitialized(@TempDir File tempDir) {
        File file = new File(tempDir, "tasks.csv");
        assertNotNull(Managers.getFileBackedManager(file));
    }
}
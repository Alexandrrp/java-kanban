package manager;

public class Managers {
    private Managers(){

    }
    public static TaskManager getDefault(){
        return new TaskManagerImplimentation();
    }
    public static HistoryManager getDefaultHistory() {
        return new MemoryHistoryManager();
    }
}
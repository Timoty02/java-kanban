package managers;

public class Managers {
    public static TaskManager getDefault() {
        return FileBackedTaskManager.loadFromFile("manager.csv");
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}

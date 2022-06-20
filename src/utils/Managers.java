package utils;

import historyManagement.HistoryManager;
import historyManagement.InMemoryHistoryManager;
import taskManagement.InMemoryTaskManager;
import taskManagement.TaskManager;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}

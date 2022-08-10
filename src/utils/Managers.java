package utils;

import historyManagement.HistoryManager;
import historyManagement.InMemoryHistoryManager;
import taskManagement.FileBackedTasksManager;
import taskManagement.InMemoryTaskManager;
import taskManagement.TaskManager;

import java.nio.file.Paths;

public class Managers {

    public static TaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static TaskManager getFileManager() {
        return new FileBackedTasksManager(Paths.get("AutoSaveFile.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}

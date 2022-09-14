package main.utils;

import main.historyManagement.HistoryManager;
import main.historyManagement.InMemoryHistoryManager;
import main.taskManagement.FileBackedTasksManager;
import main.taskManagement.InMemoryTaskManager;

import java.nio.file.Paths;

public class Managers {

    public static InMemoryTaskManager getDefault() {
        return new InMemoryTaskManager();
    }

    public static FileBackedTasksManager getFileManager() {
        return new FileBackedTasksManager(Paths.get("AutoSaveFile.csv"));
    }

    public static FileBackedTasksManager getTestFileManager() {
        return new FileBackedTasksManager(Paths.get("TestFile.csv"));
    }

    public static HistoryManager getDefaultHistory() {
        return new InMemoryHistoryManager();
    }
}

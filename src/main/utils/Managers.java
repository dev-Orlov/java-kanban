package main.utils;

import management.historyManagement.HistoryManager;
import management.historyManagement.InMemoryHistoryManager;
import management.taskManagement.FileBackedTasksManager;
import management.taskManagement.HttpTaskManager;
import management.taskManagement.InMemoryTaskManager;

import java.nio.file.Paths;

public class Managers {

    public static HttpTaskManager getDefault() {
        return new HttpTaskManager("http://localhost:8078/");
    }

    public static InMemoryTaskManager getMemoryManager() {
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

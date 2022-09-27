package main.utils;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.TypeAdapter;
import com.google.maps.internal.DurationAdapter;
import com.google.gson.stream.JsonReader;
import com.google.gson.stream.JsonWriter;
import main.historyManagement.HistoryManager;
import main.historyManagement.InMemoryHistoryManager;
import main.taskManagement.FileBackedTasksManager;
import main.taskManagement.HttpTaskManager;
import main.taskManagement.InMemoryTaskManager;

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

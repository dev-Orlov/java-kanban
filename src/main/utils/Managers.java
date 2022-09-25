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
import main.taskManagement.InMemoryTaskManager;

import java.io.IOException;
import java.nio.file.Paths;
import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

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

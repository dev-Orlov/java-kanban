package main.taskManagement;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.Exceptions.ManagerSaveException;
import main.Httpserver.KVTaskClient;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;

import java.lang.reflect.Type;
import java.nio.file.Paths;
import java.util.HashMap;
import java.util.List;

public class HttpTaskManager extends FileBackedTasksManager {

    private final KVTaskClient client;
    private final Gson gson;

    public HttpTaskManager(String url) {
        super(Paths.get("AutoSaveFile.csv"));
        this.client = new KVTaskClient(url);
        this.gson = new Gson();
    }

    @Override
    protected void save() {
        String jsonTasks = gson.toJson(tasks);
        client.put("tasks", jsonTasks);
        String jsonEpics = gson.toJson(epics); // тут сразу и эпики, и подзадачи
        client.put("epics", jsonEpics);
        String jsonHistory = gson.toJson(getHistory());
        client.put("history", jsonHistory);
    }


    public void load() throws ManagerSaveException {

        Type epicType =  new TypeToken<HashMap<Integer, Epic>>(){}.getType();
        HashMap<Integer, Epic> serializedEpics = gson.fromJson(client.load("epics"), epicType);
        for (int epicId : serializedEpics.keySet()) {
            epics.put(epicId, serializedEpics.get(epicId));
            for (Subtask subtask : getEpicIdSubtasks(epicId)) {
                serializedEpics.get(epicId).recordSubtasks(subtask);
            }
        }


        Type taskType =  new TypeToken<HashMap<Integer, Task>>(){}.getType();
        HashMap<Integer, Task> serializedTasks = gson.fromJson(client.load("tasks"), taskType);
        for (int taskId : serializedTasks.keySet()) {
                tasks.put(taskId, serializedTasks.get(taskId));
        }

        Type historyType =  new TypeToken<List<Task>>(){}.getType();
        List<Task> history = gson.fromJson(client.load("history"), historyType);
        for (int i = history.size() - 1; i >= 0; i--) {
            taskHistory.add(history.get(i));
        }
    }
}

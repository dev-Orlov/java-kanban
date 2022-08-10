package utils;

import Exceptions.ManagerSaveException;
import historyManagement.HistoryManager;
import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import tasks.TasksType;

import java.util.ArrayList;
import java.util.List;

public class CSVParser {

    public static String historyToString(HistoryManager manager) throws ManagerSaveException {
        StringBuilder historyId = new StringBuilder("");
        for (Task checkHistory : manager.getHistory()) {
            historyId.append(checkHistory.getId() + ",");
        }
        if (historyId.length() > 0) { // если история не пустая, удаляем последнюю запятую
            historyId.deleteCharAt(historyId.length() - 1);
        }
        return historyId.toString();
    }

    public static Task fromString(String value) throws ManagerSaveException {
        Task readingTask = null;
        String[] taskParameters = value.split(",");
        int maxId = 0;  // необходимо выявить максимальный id загружаемых задач для дальнейшей генерации id
        switch (TasksType.valueOf(taskParameters[1])) {
            case TASK:
                readingTask = new Task(taskParameters[2], taskParameters[4]);
                readingTask.setId(Integer.parseInt(taskParameters[0]));
                readingTask.setStatus(TaskStatuses.valueOf(taskParameters[3]));
                if (Integer.parseInt(taskParameters[0]) > maxId) {
                    maxId = Integer.parseInt(taskParameters[0]);
                }
                break;
            case EPIC:
                readingTask = new Epic(taskParameters[2], taskParameters[4]);
                readingTask.setId(Integer.parseInt(taskParameters[0]));
                readingTask.setStatus(TaskStatuses.valueOf(taskParameters[3]));
                if (Integer.parseInt(taskParameters[0]) > maxId) {
                    maxId = Integer.parseInt(taskParameters[0]);
                }
                break;
            case SUBTASK:
                readingTask = new Subtask(taskParameters[2], taskParameters[4], Integer.parseInt(taskParameters[5]));
                readingTask.setId(Integer.parseInt(taskParameters[0]));
                readingTask.setStatus(TaskStatuses.valueOf(taskParameters[3]));
                if (Integer.parseInt(taskParameters[0]) > maxId) {
                    maxId = Integer.parseInt(taskParameters[0]);
                }
                break;
        }
        Task.genId = maxId + 1;  // обновляем точку отсчета генератора id у тасков
        return readingTask;
    }

    public static List<Integer> historyFromString(String value) {
        String[] stringHistory = value.split(",");
        List<Integer> readHistory = new ArrayList<>();
        for (String historyNote : stringHistory) {
            readHistory.add(Integer.valueOf(historyNote));
        }
        return readHistory;
    }
}

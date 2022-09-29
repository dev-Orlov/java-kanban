package main.utils;

import main.exceptions.ManagerSaveException;
import management.historyManagement.HistoryManager;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;
import main.tasks.TasksType;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

public class CSVParser {

    public static String historyToString(HistoryManager manager) throws ManagerSaveException {
        StringBuilder historyId = new StringBuilder("");
        for (Task checkHistory : manager.getHistory()) {
            historyId.append(checkHistory.getId()).append(",");
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
                readingTask = new Task(taskParameters[2], taskParameters[4],
                        localDateTimeFromString(taskParameters[6]) , Integer.parseInt(taskParameters[5]));
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
                readingTask = new Subtask(taskParameters[2], taskParameters[4],
                        localDateTimeFromString(taskParameters[6]) , Integer.parseInt(taskParameters[5]),
                        Integer.parseInt(taskParameters[8]));
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

    public static LocalDateTime localDateTimeFromString(String value) {
        String[] localDateTimeParts = value.split("T");
        String[] dateParts = localDateTimeParts[0].split("-");
        String[] timeParts = localDateTimeParts[1].split(":");
        return LocalDateTime.of(Integer.parseInt(dateParts[0].trim()), Integer.parseInt(dateParts[1].trim()),
                Integer.parseInt(dateParts[2].trim()), Integer.parseInt(timeParts[0].trim()),
                Integer.parseInt(timeParts[1].trim()));
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

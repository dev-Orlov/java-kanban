package main.tasks;

import java.time.Duration;
import java.time.LocalDateTime;
import java.util.HashMap;

public class Epic extends Task {

    private HashMap<Integer, Subtask> subtasks = new HashMap<>();
    private final TasksType type = TasksType.EPIC;

    public Epic(String name, String description) {
        super(name, description, null, 0);
        // согласно ТЗ у эпика появляются параметры времени только после создания сабтасков
        this.subtasks = new HashMap<>();
    }

    public void recordSubtasks(Subtask subtask) {
        subtasks.put(subtask.id, subtask);
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void clearSubtasks() {
        subtasks.clear();
    }

    @Override
    public String toStringForPrint() {
        String printId = "Id эпика': " + id + "." + "\n";
        String printName = "Название: " + name + "."+ "\n";
        String printDescription = "Описание: " + description + "."+ "\n";
        String printStatus = "Статус: " + status + "."+ "\n";
        StringBuilder printSubtasks = new StringBuilder("Эпик содержит подзадачи: " + "\n");
        for (int subtaskId : subtasks.keySet()) {
            String printSubtask = subtasks.get(subtaskId).name + "\n";
            printSubtasks.append(printSubtask);
        }
        return printId + printName + printDescription + printStatus + printSubtasks;
    }

    @Override
    public String toString() {
        if (startTime == null) {
            return id + "," + type + "," + name + "," + status + "," + description +
                    "," + "null" + "," + "null" + "," + "null";
        }
        return id + "," + type + "," + name + "," + status + "," + description +
                "," + duration.toMinutes() + "," + startTime.format(formatter) + "," + endTime.format(formatter);
    }
    @Override
    public TasksType getType() {
        return type;
    }

    public void setDuration(Duration duration) {
        this.duration = duration;
    }

    public void getStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void getEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }

    public void setStartTime(LocalDateTime startTime) {
        this.startTime = startTime;
    }

    public void setEndTime(LocalDateTime endTime) {
        this.endTime = endTime;
    }
}

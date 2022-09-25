package main.tasks;

import java.time.LocalDateTime;

public class Subtask extends Task {

    private final int epicId;

    public Subtask(String name, String description, LocalDateTime startTime, int duration, int epicId) {
        super(name, description, startTime, duration);
        this.epicId = epicId;
        endTime = super.getEndTime();
        type = TasksType.SUBTASK;
    }

    public int getEpicId() {
        return epicId;
    }

    public String toStringForPrint() {
        String printId = "Id подзадачи': " + id + "." + "\n";
        String printName = "Название: " + name + "."+ "\n";
        String printDescription = "Описание: " + description + "."+ "\n";
        String printStatus = "Статус: " + status + "."+ "\n";
        String printEpicId = "Подзадача относится к эпику с id " + epicId + "." + "\n";
        return printId + printName + printDescription + printStatus + printEpicId;
    }

    @Override
    public String toString() {
        return id + "," + type + "," + name + "," + status + "," + description + "," +
                duration.toMinutes() + "," + startTime +
                "," + endTime + "," + epicId;
    }

    @Override
    public TasksType getType() {
        return type;
    }
}
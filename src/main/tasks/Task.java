package main.tasks;
import main.utils.TaskStatuses;

import java.time.Duration;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Task {

    protected String name;
    protected String description;
    protected TaskStatuses status;
    protected int id;
    public static int genId = 0;
    private final TasksType type = TasksType.TASK;
    protected Duration duration;
    protected LocalDateTime startTime;
    protected LocalDateTime endTime;
    protected DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd/MM/yyyy // HH:mm");

    public Task(String name, String description, LocalDateTime startTime, int duration) {
        this.name = name;
        this.description = description;
        this.status = TaskStatuses.NEW;
        this.id = generateId();
        this.startTime = startTime;
        this.duration = Duration.ofMinutes(duration);
        this.endTime = convertEndTime();
    }

    private static int generateId() {
        genId += 1;
        return genId;
    }

    public static void setGenId(int changedID) {
        genId = changedID;
    }

    public int getId() {
        return(id);
    }

    public TasksType getType() {
        return type;
    }

    public Duration getDuration() {
        return duration;
    }

    public LocalDateTime getStartTime() {
        return startTime;
    }

    public LocalDateTime getEndTime() {
        return endTime;
    }

    public void setId(int newId) {
        id = newId;
    }

    protected LocalDateTime convertEndTime() {
        if (startTime == null) {
            return null;
        } else {
            return startTime.plus(duration);
        }
    }

    public void setStatus(TaskStatuses newStatus) {
        status = newStatus;
    }

    public TaskStatuses getStatus() {
        return(status);
    }

    public String getName() {
        return(name);
    }

    public String toStringForPrint() {
        String printId = "Id задачи: " + id + "." + "\n";
        String printName = "Название: " + name + "."+ "\n";
        String printDescription = "Описание: " + description + "."+ "\n";
        String printStatus = "Статус: " + status + "."+ "\n";
        return printId + printName + printDescription + printStatus;
    }

    @Override
    public String toString() {
        return id + "," + type + "," + name + "," + status + "," + description +
                "," + duration.toMinutes() + "," + startTime.format(formatter) + "," + endTime.format(formatter);
    }
}

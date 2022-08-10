package tasks;

public class Subtask extends Task {

    private int epicId;
    private final TasksTypes type = TasksTypes.SUBTASK;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
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
        return id + "," + type + "," + name + "," + status + "," + description + "," + epicId;
    }

    @Override
    public TasksTypes getType() {
        return type;
    }
}
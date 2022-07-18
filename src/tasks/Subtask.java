package tasks;

public class Subtask extends Task {

    private int epicId;

    public Subtask(String name, String description, int epicId) {
        super(name, description);
        this.epicId = epicId;
    }

    public int getEpicId() {
        return epicId;
    }

    @Override
    public String toString() {
        String printId = "Id подзадачи': " + id + "." + "\n";
        String printName = "Название: " + name + "."+ "\n";
        String printDescription = "Описание: " + description + "."+ "\n";
        String printStatus = "Статус: " + status + "."+ "\n";
        String printEpicId = "Подзадача относится к эпику с id " + epicId + "." + "\n";
        return printId + printName + printDescription + printStatus + printEpicId;
    }
}
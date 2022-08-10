package tasks;
import utils.TaskStatuses;

public class Task {

    protected String name;
    protected String description;
    protected TaskStatuses status;
    protected int id;
    public static int genId = 0;
    private final TasksType type = TasksType.TASK;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = TaskStatuses.NEW;
        this.id = generateId();
    }

    private static int generateId() {
        genId += 1;
        return genId;
    }

    public int getId() {
        return(id);
    }

    public TasksType getType() {
        return type;
    }

    public void setId(int newId) {
        id = newId;
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
        return id + "," + type + "," + name + "," + status + "," + description;
    }
}

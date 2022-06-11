public class Task {

    protected String name;
    protected String description;
    protected String status;
    protected int id;
    private static int genId = 0;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = "NEW";
        this.id = generateId();
    }

    public void setId(int newId) {
        id = newId;
    }

    private static int generateId() {
        genId += 1;
        return genId;
    }

    @Override
    public String toString() {
        String printId = "Id задачи: " + id + "." + "\n";
        String printName = "Название: " + name + "."+ "\n";
        String printDescription = "Описание: " + description + "."+ "\n";
        String printStatus = "Статус: " + status + "."+ "\n";
        return printId + printName + printDescription + printStatus;
    }
}

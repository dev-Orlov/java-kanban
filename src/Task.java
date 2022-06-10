public class Task {

    protected String name;
    protected String description;
    protected String status;

    public Task(String name, String description) {
        this.name = name;
        this.description = description;
        status = "NEW";
    }
}

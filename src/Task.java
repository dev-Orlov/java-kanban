public class Task extends TaskTemplate {

    String status;

    public Task(String name, String description, String status) {
        super(name, description);
        this.status = status;
    }
}

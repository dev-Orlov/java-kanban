public class TaskTemplate { //класс-родитель для классов задач (абстрактный)

    String name;
    String description;

    public TaskTemplate(String name, String description) {
        this.name = name;
        this.description = description;
    }
}

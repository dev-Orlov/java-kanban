import java.util.ArrayList;

public class Epic extends Task {

    protected ArrayList<Subtask> subtasks;

    public Epic(String name, String description) {
        super(name, description);
        subtasks = new ArrayList<>();
    }

    // по ТЗ для генерации ID нужно использовать числовое поле класса manager, поэтому передаю manager при создании
    public void createSubtask(Subtask subtask, Manager manager) {
        manager.recordSubtasks(subtask);
        subtasks.add(subtask);
    }
}

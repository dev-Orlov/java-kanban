import java.util.ArrayList;

public class Epic extends TaskTemplate {

    ArrayList<Integer> subTasksId;
    String status;

    public Epic(String name, String description, ArrayList<Integer> subTasksId, Manager manager) {
        super(name, description);
        this.subTasksId = subTasksId;
        status = manager.setEpicStatus(subTasksId);
    }
}

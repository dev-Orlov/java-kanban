import taskManagement.TaskManager;
import tasks.*;

public class Main {

    public static void main(String[] args) {
    }

    public static void printHistory(TaskManager manager) {
        for (Task checkHistory : manager.getHistory()) {
            System.out.print(checkHistory.getId() + " ");
        }
        System.out.println(System.lineSeparator());
    }
}
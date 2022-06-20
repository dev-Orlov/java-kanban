package historyManagement;

import historyManagement.HistoryManager;
import tasks.Task;

import java.util.LinkedList;

public class InMemoryHistoryManager implements HistoryManager {

    private LinkedList<Task> viewsHistory = new LinkedList<>();
    private static final int VIEWS_HISTORY_LENGTH = 10;

    @Override
    public LinkedList<Task> getHistory() {
        return viewsHistory;
    }

    @Override
    public void add(Task task) {
        if (viewsHistory.size() == VIEWS_HISTORY_LENGTH) {
            viewsHistory.remove(VIEWS_HISTORY_LENGTH - 1);
            viewsHistory.add(0, task);
        } else {
            viewsHistory.add(0, task);
        }
    }
}

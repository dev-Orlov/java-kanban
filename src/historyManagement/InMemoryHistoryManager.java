package historyManagement;

import historyManagement.HistoryManager;
import tasks.Task;

import java.util.LinkedList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private LinkedList<Task> viewsHistory = new LinkedList<>();
    private static final int VIEWS_HISTORY_LENGTH = 10;

    @Override
    public List<Task> getHistory() {
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

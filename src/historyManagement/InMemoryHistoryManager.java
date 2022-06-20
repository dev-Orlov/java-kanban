package historyManagement;

import historyManagement.HistoryManager;
import tasks.Task;

import java.util.ArrayList;
import java.util.List;

public class InMemoryHistoryManager implements HistoryManager {

    private List<Task> viewsHistory = new ArrayList<>();
    public static final int VIEWS_HISTORY_LENGTH = 10;

    @Override
    public List<Task> getHistory() {
        return viewsHistory;
    }

    @Override
    public void add(Task task) {
        if (viewsHistory.size() == VIEWS_HISTORY_LENGTH) {
            viewsHistory.remove(0);
            viewsHistory.add(task);
        } else {
            viewsHistory.add(task);
        }
    }
}

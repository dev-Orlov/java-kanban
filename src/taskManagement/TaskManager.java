package taskManagement;

import tasks.Epic;
import tasks.Subtask;
import tasks.Task;
import utils.TaskStatuses;

import java.util.ArrayList;
import java.util.List;

public interface TaskManager {

    void recordTasks(Task task);

    void recordEpics(Epic epic);

    void recordSubtasks(Subtask subtask, int epicId);

    ArrayList<Task> getTaskList();

    ArrayList<Epic> getEpicList();

    ArrayList<Subtask> getSubtaskList();

    void removeTasks();

    void removeEpics();

    void removeSubtasks();

    Task getTaskById(int id);

    Epic getEpicById(int id);

    Subtask getSubtaskById(int id);

    void updateTask(int id, Task task, TaskStatuses status);

    void updateEpic(int id, Epic epic, String status);

    void updateSubtask(int id, Subtask subtask, TaskStatuses status);

    void removeTask(int id);

    void removeEpic(int id);

    void removeSubtask(int id);

    ArrayList<Subtask> getEpicIdSubtasks(int id);

    public List<Task> getHistory();
}

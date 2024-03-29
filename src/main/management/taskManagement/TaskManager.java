package management.taskManagement;

import main.exceptions.ManagerSaveException;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;
import main.utils.TaskStatuses;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public interface TaskManager {

    void recordTasks(Task task) throws ManagerSaveException;

    void recordEpics(Epic epic) throws ManagerSaveException;

    void recordSubtasks(Subtask subtask, int epicId) throws ManagerSaveException;

    ArrayList<Task> getTaskList();

    ArrayList<Epic> getEpicList();

    ArrayList<Subtask> getSubtaskList();

    void removeTasks() throws ManagerSaveException;

    void removeEpics() throws ManagerSaveException;

    void removeSubtasks() throws ManagerSaveException;

    Task getTaskById(int id) throws ManagerSaveException;

    Epic getEpicById(int id) throws ManagerSaveException;

    Subtask getSubtaskById(int id) throws ManagerSaveException;

    void updateTask(int id, Task task, TaskStatuses status) throws ManagerSaveException;

    void updateEpic(int id, Epic epic) throws ManagerSaveException;

    void updateSubtask(int id, Subtask subtask, TaskStatuses status) throws ManagerSaveException;

    void removeTask(int id) throws ManagerSaveException;

    void removeEpic(int id) throws ManagerSaveException;

    void removeSubtask(int id) throws ManagerSaveException;

    ArrayList<Subtask> getEpicIdSubtasks(int id);

    Set<Task> getPrioritizedTasks();

    List<Task> getHistory();
}

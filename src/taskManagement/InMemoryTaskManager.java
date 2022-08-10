package taskManagement;

import Exceptions.ManagerSaveException;
import historyManagement.HistoryManager;
import tasks.*;
import utils.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class InMemoryTaskManager implements TaskManager {

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HistoryManager taskHistory = Managers.getDefaultHistory();


    @Override
    public void recordTasks(Task task) throws ManagerSaveException {
        tasks.put(task.getId(), task);
    }

    @Override
    public void recordEpics(Epic epic) throws ManagerSaveException {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void recordSubtasks(Subtask subtask, int epicId) throws ManagerSaveException {
        epics.get(epicId).recordSubtasks(subtask);
        checkEpicStatus(epicId);
    }

    @Override
    public ArrayList<Task> getTaskList() {
        ArrayList<Task> taskList = new ArrayList<>();
        for (int taskId : tasks.keySet()) {
            taskList.add(tasks.get(taskId));
        }
        return taskList;
    }

    @Override
    public ArrayList<Epic> getEpicList() {
        ArrayList<Epic> epicList = new ArrayList<>();
        for (int epicId : epics.keySet()) {
            epicList.add(epics.get(epicId));
        }
        return epicList;
    }

    @Override
    public ArrayList<Subtask> getSubtaskList() {
        ArrayList<Subtask> subTaskList = new ArrayList<>();
        for (int epicId : epics.keySet()) {
            for (int subtaskId : epics.get(epicId).getSubtasks().keySet()) {
                subTaskList.add(epics.get(epicId).getSubtasks().get(subtaskId));
            }
        }
        return subTaskList;
    }

    @Override
    public void removeTasks() throws ManagerSaveException {
        tasks.clear();
    }

    @Override
    public void removeEpics() throws ManagerSaveException {
        epics.clear();
    }

    @Override
    public void removeSubtasks() throws ManagerSaveException {
        for (int epicId : epics.keySet()) {
            epics.get(epicId).clearSubtasks();
            checkEpicStatus(epicId);
        }
    }

    @Override
    public Task getTaskById(int id) throws ManagerSaveException {
        taskHistory.add(tasks.get(id));
        return tasks.get(id);
    }

    @Override
    public Epic getEpicById(int id) throws ManagerSaveException {
        taskHistory.add(epics.get(id));
        return epics.get(id);
    }

    @Override
    public Subtask getSubtaskById(int id) throws ManagerSaveException {
        int epicId = 0;
        for (int findEpicId : epics.keySet()) { // поочередно проверяем эпики, чтобы найти сабтаск
            if (epics.get(findEpicId).getSubtasks().get(id) != null) {
                epicId = epics.get(findEpicId).getSubtasks().get(id).getEpicId(); // нашли id эпика
            }
        }
        taskHistory.add(epics.get(epicId).getSubtasks().get(id));
        return epics.get(epicId).getSubtasks().get(id);
    }

    @Override
    public void updateTask(int id, Task task, TaskStatuses status) throws ManagerSaveException {
        task.setId(id);
        tasks.put(id, task);
        tasks.get(id).setStatus(status);
    }

    @Override
    public void updateEpic(int id, Epic epic, String status) throws ManagerSaveException {
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public void updateSubtask(int id, Subtask subtask, TaskStatuses status) throws ManagerSaveException {
        subtask.setId(id);
        int epicId = subtask.getEpicId();
        epics.get(epicId).getSubtasks().remove(id); // удалили сабтаск из HashMap в классе эпик
        subtask.setStatus(status); // присвоили новому объекту новый статус
        epics.get(epicId).getSubtasks().put(id, subtask); // положили в HashMap эпика новый объект
        checkEpicStatus(epicId); // проверили и переписали, если требуется, статус соответстующего эпика
    }

    private void checkEpicStatus(int epicId) {
        TaskStatuses status = setEpicStatus(epicId); // получаем новый статус эпика
        if (!(status.equals(epics.get(epicId).getStatus()))) { //если новый статус отличается, то переписываем его
            epics.get(epicId).setStatus(status);
        }
    }

    private TaskStatuses setEpicStatus(int epicId) {
        int numberOfNewTasks = 0;
        int numberOfDoneTasks = 0;
        int numberOfInProgressTasks = 0;
        TaskStatuses status = TaskStatuses.IN_PROGRESS;

        if (epics.get(epicId).getSubtasks().size() == 0) { // если у эпика нет подзадач, то статус NEW
            status = TaskStatuses.NEW;
        } else {
            for (int subtaskID : epics.get(epicId).getSubtasks().keySet()) {
                if (epics.get(epicId).getSubtasks().get(subtaskID) == null) {
                    status = TaskStatuses.NEW;
                } else {
                    switch (epics.get(epicId).getSubtasks().get(subtaskID).getStatus()) {
                        case NEW:
                            numberOfNewTasks += 1;
                            break;
                        case DONE:
                            numberOfDoneTasks += 1;
                            break;
                        case IN_PROGRESS:
                            numberOfInProgressTasks += 1;
                            break;
                    }
                }
            }
        }
        if (numberOfNewTasks != 0 &&  numberOfDoneTasks == 0 && numberOfInProgressTasks == 0) {
            status = TaskStatuses.NEW;
        } else if (numberOfNewTasks == 0 &&  numberOfDoneTasks != 0 && numberOfInProgressTasks == 0) {
            status = TaskStatuses.DONE;
        }
        return status;
    }

    @Override
    public void removeTask(int id) throws ManagerSaveException {
        taskHistory.remove(id);
        tasks.remove(id);
    }

    @Override
    public void removeEpic(int id) throws ManagerSaveException {
        for (Subtask getSubtask : getEpicIdSubtasks(id)) {
            taskHistory.remove(getSubtask.getId());
        }
        taskHistory.remove(id);
        epics.remove(id);
    }

    @Override
    public void removeSubtask(int id) throws ManagerSaveException {
        taskHistory.remove(id);
        int epicId;
        for (int findEpicId : epics.keySet()) { // поочередно проверяем эпики, чтобы найти сабтаск
            if (epics.get(findEpicId).getSubtasks().get(id) != null) {
                epicId = epics.get(findEpicId).getSubtasks().get(id).getEpicId(); // нашли id эпика
                epics.get(epicId).getSubtasks().remove(id); // удалили сабтаск из HashMap
                checkEpicStatus(epicId); // проверили и переписали, если требуется, статус  эпика
            }
        }
    }

    @Override
    public ArrayList<Subtask> getEpicIdSubtasks(int id) {
        ArrayList<Subtask> epicIdSubtasks = new ArrayList<>();
        for (int subtaskId : epics.get(id).getSubtasks().keySet()) {
            epicIdSubtasks.add(epics.get(id).getSubtasks().get(subtaskId));
        }
        return epicIdSubtasks;
    }

    public List<Task> getHistory() {
        return taskHistory.getHistory();
    }
}

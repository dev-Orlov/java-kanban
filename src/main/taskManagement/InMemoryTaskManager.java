package main.taskManagement;

import main.Exceptions.ManagerSaveException;
import main.historyManagement.HistoryManager;
import main.tasks.*;
import main.utils.*;

import java.io.File;
import java.time.Duration;
import java.time.LocalDateTime;
import java.util.*;

public class InMemoryTaskManager implements TaskManager {

    protected HashMap<Integer, Task> tasks = new HashMap<>();
    protected HashMap<Integer, Epic> epics = new HashMap<>();
    protected HistoryManager taskHistory = Managers.getDefaultHistory();


    @Override
    public void recordTasks(Task task) throws ManagerSaveException {
        if (checkingTaskTimeInterval(task)) {
            tasks.put(task.getId(), task);
        } else {
            System.out.println("Задача с id " + task.getId() +
                    " не записана, её время выполнения пересекается с уже созданными задачами");
        }
    }

    @Override
    public void recordEpics(Epic epic) throws ManagerSaveException {
        epics.put(epic.getId(), epic);
    }

    @Override
    public void recordSubtasks(Subtask subtask, int epicId) throws ManagerSaveException {
        if (checkingTaskTimeInterval(subtask)) {
            epics.get(epicId).recordSubtasks(subtask);
            checkEpicStatus(epicId);
            setEpicTime(epicId);
        } else {
            System.out.println("Подзадача с id " + subtask.getId() +
                    " не записана, её время выполнения пересекается с уже созданными задачами");
        }
    }

    @Override
    public ArrayList<Task> getTaskList() {
        return new ArrayList<>(tasks.values());
    }

    @Override
    public ArrayList<Epic> getEpicList() {
        return new ArrayList<>(epics.values());
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
        for (Task task : getTaskList()) {
            removeTask(task.getId());
        }
    }

    @Override
    public void removeEpics() throws ManagerSaveException {
        for (Epic epic : getEpicList()) {
            removeEpic(epic.getId());
        }
    }

    @Override
    public void removeSubtasks() throws ManagerSaveException {
        for (Subtask subtask : getSubtaskList()) {
            removeSubtask(subtask.getId());
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
        if (checkingTaskTimeInterval(task)) {
            task.setId(id);
            tasks.put(id, task);
            tasks.get(id).setStatus(status);
        } else {
            System.out.println("Задача с id " + task.getId() +
                    " не изменена, её новое время выполнения пересекается с уже созданными задачами");
        }
    }

    @Override
    public void updateEpic(int id, Epic epic) throws ManagerSaveException {
        epic.setId(id);
        epics.put(id, epic);
    }

    @Override
    public void updateSubtask(int id, Subtask subtask, TaskStatuses status) throws ManagerSaveException {
        if (checkingTaskTimeInterval(subtask)) {
            subtask.setId(id);
            int epicId = subtask.getEpicId();
            epics.get(epicId).getSubtasks().remove(id); // удалили сабтаск из HashMap в классе эпик
            subtask.setStatus(status); // присвоили новому объекту новый статус
            epics.get(epicId).getSubtasks().put(id, subtask); // положили в HashMap эпика новый объект
            checkEpicStatus(epicId); // проверили и переписали, если требуется, статус соответстующего эпика
            setEpicTime(epicId);
        } else {
            System.out.println("Подзадача с id " + subtask.getId() +
                    " не изменена, её новое время выполнения пересекается с уже созданными задачами");
        }
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

    private void setEpicTime(int epicId) {
        Duration duration = Duration.ofMinutes(0);
        LocalDateTime startTime = LocalDateTime.of(999999999, 12, 31, 23, 59);
        LocalDateTime endTime = LocalDateTime.of(-999999999, 1, 1, 0, 0);
        // задаём переменные мин и макс поддерживаемого в классе LocalDateTime времени для дальнейшего сравнения

        if (epics.get(epicId).getSubtasks().size() == 0) { // если у эпика нет подзадач, то даты и длительность = null
            epics.get(epicId).setDuration(null);
            epics.get(epicId).setStartTime(null);
            epics.get(epicId).setEndTime(null);
        } else {
            for (int subtaskID : epics.get(epicId).getSubtasks().keySet()) {
                duration = duration.plus(epics.get(epicId).getSubtasks().get(subtaskID).getDuration());
                endTime = epics.get(epicId).getSubtasks().get(subtaskID).getEndTime();
                if (epics.get(epicId).getSubtasks().get(subtaskID).getStartTime().isBefore(startTime)) {
                    startTime = epics.get(epicId).getSubtasks().get(subtaskID).getStartTime();
                }
                if (epics.get(epicId).getSubtasks().get(subtaskID).getEndTime().isAfter(endTime)) {
                    endTime = epics.get(epicId).getSubtasks().get(subtaskID).getEndTime();
                }
            }
        }
        epics.get(epicId).setDuration(duration);
        epics.get(epicId).setStartTime(startTime);
        epics.get(epicId).setEndTime(endTime);
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
                setEpicTime(epicId);
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

    @Override
    public Set<Task> getPrioritizedTasks() {
        Comparator<Task> taskTimeComparator = new Comparator<>() {
            @Override
            public int compare(Task task1, Task task2) {
                if (task1.getStartTime() == null) {
                    return 1;
                } else if (task2.getStartTime() == null) {
                    return -1;
                } else {
                    return task1.getStartTime().compareTo(task2.getStartTime());
                }
            }
        };

        Set<Task> priorityTaskList = new TreeSet<>(taskTimeComparator);
        priorityTaskList.addAll(getTaskList());
        priorityTaskList.addAll(getSubtaskList());
        return priorityTaskList;
    }

    private boolean checkingTaskTimeInterval(Task task) {
        if (task.getStartTime() != null || task.getEndTime() != null) {
            for (Task checkingTask : getPrioritizedTasks()) {
                if (task.getStartTime().equals(checkingTask.getStartTime()) ||
                        task.getEndTime().equals(checkingTask.getEndTime())) {
                    return false;
                } else if ((task.getStartTime().isBefore(checkingTask.getStartTime())
                        && task.getEndTime().isAfter(checkingTask.getStartTime()))) {
                    return false;
                } else if ((task.getStartTime().isBefore(checkingTask.getEndTime())
                        && task.getEndTime().isAfter(checkingTask.getEndTime()))) {
                    return false;
                } else if ((task.getStartTime().isAfter(checkingTask.getStartTime())
                        && task.getEndTime().isBefore(checkingTask.getEndTime()))) {
                    return false;
                }
            }
        }
        return true;
    }

    public List<Task> getHistory() {
        return taskHistory.getHistory();
    }
}

import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();

    public void recordTasks(Task task) {
        tasks.put(task.id, task);
    }

    public void recordEpics(Epic epic) {
        epics.put(epic.id, epic);
    }

    public ArrayList<Task> getTaskList() {
        ArrayList<Task> taskList = new ArrayList<>();
        for (int taskId : tasks.keySet()) {
            taskList.add(tasks.get(taskId));
        }
        return taskList;
    }

    public ArrayList<Epic> getEpicList() {
        ArrayList<Epic> epicList = new ArrayList<>();
        for (int epicId : epics.keySet()) {
            epicList.add(epics.get(epicId));
        }
        return epicList;
    }

    public ArrayList<Subtask> getSubtaskList() {
        ArrayList<Subtask> subTaskList = new ArrayList<>();
        for (int epicId : epics.keySet()) {
            for (int subtaskId : epics.get(epicId).getSubtasks().keySet()) {
                subTaskList.add(epics.get(epicId).getSubtasks().get(subtaskId));
            }
        }
        return subTaskList;
    }

    public void removeTasks() {
        tasks.clear();
    }

    public void removeEpics() {
        epics.clear();
    }

    public void removeSubtasks() {
        for (int epicId : epics.keySet()) {
            epics.get(epicId).clearSubtasks();
            epics.get(epicId).status = "NEW"; // так как по ТЗ, если у эпика нет подзадач, то он имеет статус NEW
        }
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void updateTask(int id, Task task, String status) {
        task.id = id;
        tasks.put(id, task);
        tasks.get(id).status = status;
    }

    public void updateEpic(int id, Epic epic, String status) {
        epic.id = id;
        epics.put(id, epic);
    }

    public void updateSubtask(int id, Subtask subtask, String status) {
        subtask.id = id;
        int epicId = subtask.getEpicId();
        epics.get(epicId).getSubtasks().remove(id); // удалили сабтаск из HashMap в классе эпик
        subtask.status = status; // присвоили новому объекту новый статус
        epics.get(epicId).getSubtasks().put(id, subtask); // положили в HashMap эпика новый объект
        checkEpicStatus(epicId); // проверили и переписали, если требуется, статус соответстующего эпика
    }

    private void checkEpicStatus(int epicId) {
        String status = setEpicStatus(epicId); // получаем новый статус эпика
        if (!(status.equals(epics.get(epicId).status))) { //если новый статус отличается, то переписываем его
            epics.get(epicId).status = status;
        }
    }

    public String setEpicStatus(int epicId) {
        int numberOfNewTasks = 0;
        int numberOfDoneTasks = 0;
        int numberOfInProgressTasks = 0;
        String status = "IN_PROGRESS"; // согласно ТЗ, это дефолтный статус, если не выполняются обратные условия

        if (epics.get(epicId).getSubtasks().size() == 0) { // если у эпика нет подзадач, то статус NEW
            status = "NEW";
        } else {
            for (int subtaskID : epics.get(epicId).getSubtasks().keySet()) {
                if (epics.get(epicId).getSubtasks().get(subtaskID) == null) {
                    status = "NEW";
                } else {
                    switch (epics.get(epicId).getSubtasks().get(subtaskID).status) {
                        case "NEW":
                            numberOfNewTasks += 1;
                            break;
                        case "DONE":
                            numberOfDoneTasks += 1;
                            break;
                        case "IN_PROGRESS":
                            numberOfInProgressTasks += 1;
                            break;
                    }
                }
            }
        }
        if (numberOfNewTasks != 0 &&  numberOfDoneTasks == 0 && numberOfInProgressTasks == 0) {
            status = "NEW";
        } else if (numberOfNewTasks == 0 &&  numberOfDoneTasks != 0 && numberOfInProgressTasks == 0) {
            status = "DONE";
        }
        return status;
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeEpic(int id) {
        epics.remove(id);
    }

    public void removeSubtask(int id) {
        int epicId;
        for (int findEpicId : epics.keySet()) { // поочередно проверяем эпики, чтобы найти сабтаск
            if (epics.get(findEpicId).getSubtasks().get(id) != null) {
                epicId = epics.get(findEpicId).getSubtasks().get(id).getEpicId(); // нашли id эпика
                epics.get(epicId).getSubtasks().remove(id); // удалили сабтаск из HashMap
                checkEpicStatus(epicId); // проверили и переписали, если требуется, статус  эпика
            }
        }
    }

    public ArrayList<Subtask> getEpicIdSubtasks(int id) {
        ArrayList<Subtask> epicIdSubtasks = new ArrayList<>();
        for (int subtaskId : epics.get(id).getSubtasks().keySet()) {
            epicIdSubtasks.add(epics.get(id).getSubtasks().get(subtaskId));
        }
        return epicIdSubtasks;
    }
}

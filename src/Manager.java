import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    private HashMap<Integer, Task> tasks = new HashMap<>();
    private HashMap<Integer, Epic> epics = new HashMap<>();
    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    private int taskNumber = 0;

    public int getTaskNumber() {
        taskNumber = taskNumber + 1;
        return taskNumber;
    }

    public void recordTasks(Task task) {
        int id = getTaskNumber();
        tasks.put(id, task);
    }

    public void recordEpics(Epic epic) {
        int id = getTaskNumber();
        epics.put(id, epic);
    }

    public void recordSubtasks(Subtask subtask) {
        int id = getTaskNumber();
        subtasks.put(id, subtask);
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
            subTaskList.addAll(epics.get(epicId).subtasks);
        }
        return subTaskList;
    }

    public void removeTasks() {
        tasks.clear();
    }

    public void removeEpics() {
        epics.clear();
    }

    public void removeSubtasks(Manager manager) {
        for (int epicId : epics.keySet()) {
            epics.get(epicId).subtasks.clear();
            epics.get(epicId).status = "NEW"; // так как по ТЗ, если у эпика нет подзадач, то он имеет статус NEW
        }
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public void updateTask(int id, Task task, String status) {
        tasks.put(id, task);
        tasks.get(id).status = status;
    }

    public void updateEpic(int id, Epic epic, String status) {
        epics.put(id, epic);
    }

    // метод реализую именно в manager, потому что тут удобнее работать с полем id сабтаска
    public void updateSubtask(int id, Subtask subtask, String status) {
        epics.get(subtask.epicId).subtasks.remove(subtasks.get(id)); // удалили сабтаск из списка в классе эпик
        subtask.status = status; // присвоили новому объекту новый статус
        epics.get(subtask.epicId).subtasks.add(subtask); // положили в список эпика список новый объект
        subtasks.put(id, subtask); // сохранили объект в HashMap
        checkEpicStatus(id); // проверили и переписали, если требуется, статус соответстующего эпика
    }

    private void checkEpicStatus(int id) {
        int epicId = subtasks.get(id).epicId; // переменная для упрочтения чтения. Получаем id эпика у подзадачи.
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

        if (epics.get(epicId).subtasks.size() == 0) { // если у эпика нет подзадач, то статус NEW
            status = "NEW";
        } else {
            for (Subtask subtask : epics.get(epicId).subtasks) { // считаем количество подзадач эпика с разным статусом
                if (epics.get(epicId).subtasks == null) { // если подзадачи ещё не успели создать, то статус NEW
                    status = "NEW";
                } else {
                    switch (subtask.status) {
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

    // метод реализую именно в manager, потому что тут удобнее работать с полем id сабтаска
    public void removeSubtask(int id) {
        int epicId = subtasks.get(id).epicId; // выяснили, к какому эпику относится подзадача
        subtasks.remove(id); // удалили подзадачу
        checkEpicStatus(id); // проверили и переписали, если требуется, статус  эпика
    }

    // раз уж все методы реализую тут, то и этот оставил в manager для порядка, хотя перенести в эпик и не сложно.
    public ArrayList<Subtask> getEpicIdSubtasks(int id) {
        return epics.get(id).subtasks;
    }
}

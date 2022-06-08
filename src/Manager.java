import java.util.ArrayList;
import java.util.HashMap;

public class Manager {

    HashMap<Integer, Task> tasks = new HashMap<>();
    HashMap<Integer, Subtask> subtasks = new HashMap<>();
    HashMap<Integer, Epic> epics = new HashMap<>();
    int taskNumber = 0;

    public int getTaskNumber() {
        taskNumber = taskNumber + 1;
        return taskNumber;
    }

    public void recordTasks(Task task) {
        int id = getTaskNumber();
        tasks.put(id, task);
    }

    public void recordSubtasks(Subtask subtask, Manager manager) {
        int id = getTaskNumber();
        subtasks.put(id, subtask);
        int epicId = subtasks.get(id).epicId; // выяснили, к какому эпику относится подзадача
        changeEpicAfterRecordSubtask(id, epicId, manager); // так как появилась новая подзадача, может измениться эпик
    }

    private void changeEpicAfterRecordSubtask(int id, int epicId, Manager manager) {
        if (epics.get(epicId) != null) { // ошибка может возникнуть, если подзадачу создавали раньше эпика
            ArrayList<Integer> newSubTasksId = epics.get(epicId).subTasksId; // создали новый лист подзадач эпика
            if (!(newSubTasksId.contains(id))) { // если подзадача не указывалась при создании эпика, то добавляем её
                newSubTasksId.add(id); // добавили нужную подзадачу из эпика в новый список
                Epic newEpic = new Epic(epics.get(epicId).name, epics.get(epicId).description,
                        newSubTasksId, manager); // создали новый объект эпика
                updateEpic(epicId, newEpic); // переписали эпик
            }
        }
    }

    public void recordEpics(Epic epic) {
        int id = getTaskNumber();
        epics.put(id, epic);
    }

    public String setEpicStatus(ArrayList<Integer> subTasksId) {
        int numberOfNewTasks = 0;
        int numberOfDoneTasks = 0;
        int numberOfInProgressTasks = 0;
        String status = "IN_PROGRESS"; // согласно ТЗ, это дефолтный статус, если не выполняются обратные условия

        if (subTasksId.size() == 0) { // если у эпика нет подзадач, то статус NEW
            status = "NEW";
        } else {
            for (int checkSubtask : subTasksId) { // считаем количество подзадач эпика с разным статусом
                if (subtasks.get(checkSubtask) == null) { // если подзадачи ещё не успели создать, то статус NEW
                    status = "NEW";
                } else {
                    if (subtasks.get(checkSubtask).status.equals("NEW")) {
                        numberOfNewTasks += 1;
                    } else if (subtasks.get(checkSubtask).status.equals("DONE")) {
                        numberOfDoneTasks += 1;
                    } else if (subtasks.get(checkSubtask).status.equals("IN_PROGRESS")) {
                        numberOfInProgressTasks += 1;
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

    public ArrayList<Task> getTaskList() {
        ArrayList<Task> taskList = new ArrayList<>();
        for (int taskId : tasks.keySet()) {
            taskList.add(tasks.get(taskId));
        }
        return taskList;
    }

    public ArrayList<Subtask> getSubtaskList() {
        ArrayList<Subtask> subTaskList = new ArrayList<>();
        for (int subTaskId : subtasks.keySet()) {
            subTaskList.add(subtasks.get(subTaskId));
        }
        return subTaskList;
    }

    public ArrayList<Epic> getEpicList() {
        ArrayList<Epic> epicList = new ArrayList<>();
        for (int epicId : epics.keySet()) {
            epicList.add(epics.get(epicId));
        }
        return epicList;
    }

    public void removeTasks() {
        tasks.clear();
    }

    public void removeSubtasks(Manager manager) {
        subtasks.clear(); // удалили подзадачи
        changeEpics(manager); // так как все подзадачи удалены, должны измениться статусы эпиков
    }

    private void changeEpics(Manager manager) {
        for (int epicId : epics.keySet()) {
            ArrayList<Integer> emptySubtasks = new ArrayList<>(); // список подзадач в эпиках теперь тоже пустой
            Epic newEpic = new Epic(epics.get(epicId).name, epics.get(epicId).description, emptySubtasks, manager);
            updateEpic(epicId, newEpic); // переписали объекты эпиков уже с новыми статусами
        }
    }

    public void removeEpics(Manager manager) {
        epics.clear(); // удалили эпики
        removeSubtasks(manager); // если удалились эпики, значит должны удалиться и подзадачи эпиков
    }

    public Task getTaskById(int id) {
        return tasks.get(id);
    }

    public Subtask getSubtaskById(int id) {
        return subtasks.get(id);
    }

    public Epic getEpicById(int id) {
        return epics.get(id);
    }

    public void updateTask(int id, Task task) {
        tasks.put(id, task);
    }

    public void updateSubtask(int id, Subtask subTask, Manager manager) {
        subtasks.put(id, subTask); // обновили Subtask
        checkEpicStatus(id, manager); // проверили и переписали, если требуется, статус соответстующего эпика
    }

    private void checkEpicStatus(int id, Manager manager) {
        int epicId = subtasks.get(id).epicId; // переменная для упрочтения чтения. Получаем id эпика у подзадачи.
        String status = setEpicStatus(epics.get(epicId).subTasksId); // получаем новый статус эпика
        if (!(status.equals(epics.get(epicId).status))) { //если новый статус отличается, то переписываем его
            Epic newEpic = new Epic(epics.get(epicId).name, epics.get(epicId).description,
                    epics.get(epicId).subTasksId, manager); // создали новый объект эпика
            updateEpic(epicId, newEpic); // переписали эпик
        }
    }

    public void updateEpic(int id, Epic epic) {
        epics.put(id, epic);
    }

    public void removeTask(int id) {
        tasks.remove(id);
    }

    public void removeSubtask(int id, Manager manager) {
        int epicId = subtasks.get(id).epicId; // выяснили, к какому эпику относится подзадача
        subtasks.remove(id); // удалили подзадачу
        changeEpicAfterRemoveSubtask(id, epicId, manager); // проверили и переписали, если требуется, статус  эпика
    }

    private void changeEpicAfterRemoveSubtask(int id, int epicId, Manager manager) {
        if (epics.get(epicId) != null) { // ошибка может возникнуть, если подзадачу создавали раньше эпика
            ArrayList<Integer> newSubTasksId = epics.get(epicId).subTasksId; // создали новый лист подзадач эпика
            newSubTasksId.remove(new Integer(id)); // удалили нужную подзадачу из эпика
            Epic newEpic = new Epic(epics.get(epicId).name, epics.get(epicId).description,
                    newSubTasksId, manager); // создали новый объект эпика
            updateEpic(epicId, newEpic); // переписали эпик
        }
    }

    public void removeEpic(int id) {
        ArrayList<Integer> subTasksId = epics.get(id).subTasksId; // нашли подзадачи удаляемого эпика
        epics.remove(id); // удалили эпик
        for (int subtask : subTasksId) {
            subtasks.remove(subtask); // удалили все подзадачи удаленного эпика
        }
    }

    public ArrayList<Subtask> getEpicIdSubtasks(int id) {
        ArrayList<Subtask> epicsIdSubtasks = new ArrayList<>();
        for (int subtask : epics.get(id).subTasksId) {
            epicsIdSubtasks.add(subtasks.get(subtask));
        }
        return epicsIdSubtasks;
    }
}


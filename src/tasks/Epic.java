package tasks;

import tasks.Task;

import java.util.HashMap;

public class Epic extends Task {

    private HashMap<Integer, Subtask> subtasks = new HashMap<>();

    public Epic(String name, String description) {
        super(name, description);
        subtasks = new HashMap<>();
    }

    public void recordSubtasks(Subtask subtask) {
        subtasks.put(subtask.id, subtask);
    }

    public HashMap<Integer, Subtask> getSubtasks() {
        return subtasks;
    }

    public void clearSubtasks() {
        subtasks.clear();
    }

    @Override
    public String toString() {
        String printId = "Id эпика': " + id + "." + "\n";
        String printName = "Название: " + name + "."+ "\n";
        String printDescription = "Описание: " + description + "."+ "\n";
        String printStatus = "Статус: " + status + "."+ "\n";
        String printSubtasks = "Эпик содержит подзадачи: " + "\n";
        for (int subtaskId : subtasks.keySet()) {
            String printSubtask = subtasks.get(subtaskId).name + "\n";
            printSubtasks += printSubtask;
        }
        return printId + printName + printDescription + printStatus + printSubtasks;
    }
}

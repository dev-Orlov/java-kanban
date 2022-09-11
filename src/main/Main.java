package main;

import main.Exceptions.ManagerSaveException;
import main.taskManagement.TaskManager;
import main.tasks.*;
import main.utils.Managers;

import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws ManagerSaveException {

        //тесты для ТЗ №7

        TaskManager manager = Managers.getDefault();
        manager.recordTasks(new Task("Задача №1", "Описание задачи №1",
                LocalDateTime.of(2022, 9, 11, 22, 10), 30));
    }

    public static void printHistory(TaskManager manager) {
        for (Task checkHistory : manager.getHistory()) {
            System.out.print(checkHistory.getId() + " ");
        }
        System.out.println(System.lineSeparator());
    }
}
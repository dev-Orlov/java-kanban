package main;

import main.Exceptions.ManagerSaveException;
import main.Httpserver.KVServer;
import main.taskManagement.HttpTaskManager;
import main.taskManagement.TaskManager;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;
import main.utils.Managers;

import java.io.IOException;
import java.time.LocalDateTime;

public class Main {

    public static void main(String[] args) throws ManagerSaveException, IOException {
        new KVServer().start();

        TaskManager manager = Managers.getDefault();

        manager.recordTasks(new Task("Задача №1", "Описание задачи №1",
                LocalDateTime.of(2025, 9, 12, 10, 0), 30));
        manager.recordTasks(new Task("Задача №2", "Описание задачи №2",
                LocalDateTime.of(2024, 9, 12, 14, 20), 360));
        manager.recordEpics(new Epic("Эпик №1 с тремя подзадачами", "Описание эпика №1"));
        manager.recordSubtasks((new Subtask("Подзадача №1 эпика №1",
                "описание подзадачи №1 эпика №1",
                LocalDateTime.of(2023, 9, 1, 8, 0), 60, 3)), 3);
        manager.recordSubtasks((new Subtask("Подзадача №2 эпика №1",
                "описание подзадачи №2 эпика №1",
                LocalDateTime.of(2022, 10, 13, 8, 0), 20, 3)), 3);
        manager.recordSubtasks((new Subtask("Подзадача №3 эпика №1",
                "описание подзадачи №3 эпика №1",
                LocalDateTime.of(2021, 11, 20, 8, 0), 45, 3)), 3);
        manager.recordEpics(new Epic("Эпик №2 без подзадач", "Описание эпика №2"));

        manager.getTaskById(2);
        manager.getSubtaskById(6);
        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getTaskById(1);
        manager.getEpicById(3);
        manager.getTaskById(2);
        manager.getEpicById(7);
        manager.getSubtaskById(4);
        manager.getTaskById(1);
        manager.getEpicById(7);
        manager.getSubtaskById(5);

        for (Task task5 : manager.getHistory()) {
            System.out.print(task5.getId());
        }

        HttpTaskManager manager2 = Managers.getDefault();

        manager2.load();
        for (Task task5 : manager2.getHistory()) {
            System.out.print(task5.getId());
        }
    }
}
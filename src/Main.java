import taskManagement.TaskManager;
import tasks.*;
import utils.*;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        System.out.println("Тестируем программу согласно ТЗ.");
        System.out.println(System.lineSeparator());
        System.out.println("Создаем 2 задачи, эпик с 3 подзадачами и эпик без подзадач.");
        System.out.println("*ИДЁТ СОЗДАНИЕ*");

        manager.recordTasks(new Task("Задача №1", "Описание задачи №1"));

        manager.recordTasks(new Task("Задача №2", "Описание задачи №2"));

        manager.recordEpics(new Epic("Эпик №1 с тремя подзадачами", "Описание эпика №1"));

        manager.recordSubtasks((new Subtask("Подзадача №1 эпика №1",
                "описание подзадачи №1 эпика №1",3)),3);

        manager.recordSubtasks((new Subtask("Подзадача №2 эпика №1",
                "описание подзадачи №2 эпика №1",3)),3);

        manager.recordSubtasks((new Subtask("Подзадача №3 эпика №1",
                "описание подзадачи №3 эпика №1",3)),3);

        manager.recordEpics(new Epic("Эпик №2 без подзадач", "Описание эпика №2"));

        System.out.println("*ЗАДАЧИ УСПЕШНО СОЗДАНЫ*");
        System.out.println(System.lineSeparator());
        System.out.println("Запрашиваем созданные задачи несколько раз в разном порядке");

        System.out.println("Согласно ТЗ выводим историю после каждого запроса");

        manager.getTaskById(1);
        printHistory(manager);

        manager.getTaskById(1);
        printHistory(manager);

        manager.getSubtaskById(5);
        printHistory(manager);

        manager.getEpicById(7);
        printHistory(manager);

        manager.getTaskById(1);
        printHistory(manager);

        manager.getTaskById(2);
        printHistory(manager);

        manager.getSubtaskById(6);
        printHistory(manager);

        manager.getTaskById(1);
        printHistory(manager);

        manager.getTaskById(2);
        printHistory(manager);

        manager.getTaskById(1);
        printHistory(manager);

        manager.getEpicById(3);
        printHistory(manager);

        manager.getTaskById(2);
        printHistory(manager);

        manager.getEpicById(7);
        printHistory(manager);

        manager.getSubtaskById(4);
        printHistory(manager);

        manager.getTaskById(1);
        printHistory(manager);

        manager.getEpicById(7);
        printHistory(manager);

        manager.getSubtaskById(5);
        printHistory(manager);

        System.out.println("Удаляем задачу и проверяем вывод истории");

        manager.removeTask(2);
        printHistory(manager);

        System.out.println("Удаляем эпик с тремя подзадачами и проверяем вывод истории");
        manager.removeEpic(3);
        printHistory(manager);
    }

    public static void printHistory(TaskManager manager) {
        for (Task checkHistory : manager.getHistory()) {
            System.out.print(checkHistory.getId() + " "); // для удобства проверки печатаю только id
        }
        System.out.println(System.lineSeparator());
    }
}
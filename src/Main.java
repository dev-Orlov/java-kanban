import taskManagement.TaskManager;
import tasks.*;
import utils.*;

public class Main {

    public static void main(String[] args) {
        TaskManager manager = Managers.getDefault();

        System.out.println("Тестируем программу согласно ТЗ.");
        System.out.println(System.lineSeparator());
        System.out.println("Создаем 2 задачи, эпик с 2 подзадачами и эпик с 1 подзадачей.");
        System.out.println("*ИДЁТ СОЗДАНИЕ*");

        Task task = new Task("Пойти спать", "почему-то всегда забываю");
        manager.recordTasks(task);

        task = new Task("Оплатить YouTube премиум", "теперь это целый квест");
        manager.recordTasks(task);

        Epic epic = new Epic("Погладить собаку", "пёс очень милый, нужно чаще его гладить");
        manager.recordEpics(epic);

        Subtask subtask = new Subtask("Почесать за правым ухом", "чесать нужно медленно",3);
        manager.recordSubtasks(subtask, 3);

        subtask = new Subtask("Почесать животик",
                "там он не достает, поэтому забавно кайфует",3);
        manager.recordSubtasks(subtask, 3);

        epic = new Epic("Стать программистом", "зря я 8 лет строил карьеру в SMM");
        manager.recordEpics(epic);

        subtask = new Subtask("Починить микроволновку",
                "жена сказала, что программисты это умеют",6);
        manager.recordSubtasks(subtask, 6);

        System.out.println("*ЗАДАЧИ УСПЕШНО СОЗДАНЫ*");
        System.out.println(System.lineSeparator());
        System.out.println("Теперь печатаем списки созданных задач.");

        for (Task getTask : manager.getTaskList()) {
            System.out.println(getTask);
        }
        System.out.println(System.lineSeparator());

        for (Epic getEpic : manager.getEpicList()) {
            System.out.println(getEpic);
        }
        System.out.println(System.lineSeparator());

        for (Subtask getSubtask : manager.getSubtaskList()) {
            System.out.println(getSubtask);
        }
        System.out.println(System.lineSeparator());
        System.out.println("Меняем статусы созданных объектов.");

        task = new Task("Пойти спать", "почему-то всегда забываю");
        manager.updateTask(1, task, TaskStatuses.IN_PROGRESS);

        task = new Task("Оплатить YouTube премиум", "теперь это целый квест");
        manager.updateTask(2, task, TaskStatuses.DONE);

        subtask = new Subtask("Почесать за правым ухом", "чесать нужно медленно", 3);
        manager.updateSubtask(4, subtask, TaskStatuses.DONE);

        subtask = new Subtask("Почесать животик",
                "там он не достает, поэтому забавно кайфует", 3);
        manager.updateSubtask(5, subtask, TaskStatuses.DONE);

        subtask = new Subtask("Починить микроволновку",
                "жена сказала, что программисты это умеют", 6);
        manager.updateSubtask(7, subtask, TaskStatuses.IN_PROGRESS);
        System.out.println("*СТАТУСЫ ПОДЗАДАЧ ИЗМЕНЕНЫ*");
        System.out.println(System.lineSeparator());

        System.out.println("Печатаем новые статусы");

        for (Task getTask : manager.getTaskList()) {
            System.out.println("Статус задачи " + getTask.getName() + " изменился на " + getTask.getStatus() + ".");
        }
        System.out.println(System.lineSeparator());

        for (Epic getEpic : manager.getEpicList()) {
            System.out.println("Статус задачи " + getEpic.getName() + " изменился на " + getEpic.getStatus() + ".");
        }
        System.out.println(System.lineSeparator());

        for (Subtask getSubtask : manager.getSubtaskList()) {
            System.out.println("Статус задачи " + getSubtask.getName() + " изменился на " +
                    getSubtask.getStatus() + ".");
        }
        System.out.println(System.lineSeparator());
        System.out.println("И, наконец, удаляем 1 задачу и 1 эпик");

        manager.removeTask(2);
        manager.removeEpic(3);
        System.out.println("*ЗАДАЧИ УДАЛЕНЫ*");
        System.out.println(System.lineSeparator());
        System.out.println("Теперь задачи выглядят вот так:");

        for (Task getTask : manager.getTaskList()) {
            System.out.println(getTask);
        }
        System.out.println(System.lineSeparator());

        for (Epic getEpic : manager.getEpicList()) {
            System.out.println(getEpic);
        }
        System.out.println(System.lineSeparator());

        for (Subtask getSubtask : manager.getSubtaskList()) {
            System.out.println(getSubtask);
        }

        System.out.println("Тестируем историю просмотра последних 10 задач.");
        System.out.println("*ПРОСМАТРИВАЕМ ЗАДАЧИ С ID 1,6,7,7,7,7,6,1,1,1,7,7,7,7,7");
        manager.getTaskById(1);
        manager.getEpicById(6);
        manager.getSubtaskById(7);
        manager.getSubtaskById(7);
        manager.getSubtaskById(7);
        manager.getSubtaskById(7);
        manager.getEpicById(6);
        manager.getTaskById(1);
        manager.getTaskById(1);
        manager.getTaskById(1);
        manager.getSubtaskById(7);
        manager.getSubtaskById(7);
        manager.getSubtaskById(7);
        manager.getSubtaskById(7);
        manager.getSubtaskById(7);

        System.out.print("Id последних 10 просмотренных задач: ");
        for (Task checkHistory : manager.getHistory()) {
            System.out.print(checkHistory.getId() + " "); // для удобства проверки печатаю только id
        }
    }
}

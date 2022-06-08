import java.util.ArrayList;

public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        // в методе для тестирования много дублирования, так как согласно ТЗ, тест нужно проводить только в main.

        System.out.println("Тестируем программу согласно ТЗ.");
        System.out.println(System.lineSeparator());
        System.out.println("Создаем 2 задачи, эпик с 2 подзадачами и эпик с 1 подзадачей.");
        System.out.println("*ИДЁТ СОЗДАНИЕ*");

        Task task = new Task("Пойти спать", "почему-то всегда забываю", "NEW");
        manager.recordTasks(task);

        task = new Task("Оплатить YouTube премиум", "теперь это целый квест", "NEW");
        manager.recordTasks(task);

        ArrayList<Integer> subTasksId = new ArrayList<>(); // согласно ТЗ, эпик знает свои подзадачи.
        subTasksId.add(4);
        subTasksId.add(5); // создали и наполнили список будущих подзадач
        Epic epic = new Epic("Погладить собаку", "пёс очень милый, нужно чаще его гладить",
                subTasksId, manager); // создали объект эпика
        manager.recordEpics(epic); //записали объект в HashMap с эпиками класса manager

        Subtask subtask = new Subtask("Почесать за правым ухом", "чесать нужно медленно",
                "NEW", 3); // создали объект подзадачи
        manager.recordSubtasks(subtask, manager); // записали объект подзадачи в HashMap с подзадачами класса manager

        subtask = new Subtask("Почесать животик", "там он не достает, поэтому забавно кайфует",
                "NEW", 3);
        manager.recordSubtasks(subtask, manager);

        subTasksId = new ArrayList<>();
        subTasksId.add(7);
        epic = new Epic("Стать программистом", "зря я 8 лет строил карьеру в SMM",
                subTasksId, manager);
        manager.recordEpics(epic);

        subtask = new Subtask("Починить микроволновку", "жена сказала, что программисты это умеют",
                "NEW", 6);
        manager.recordSubtasks(subtask, manager);

        System.out.println("*ЗАДАЧИ УСПЕШНО СОЗДАНЫ*");
        System.out.println(System.lineSeparator());
        System.out.println("Теперь печатаем списки созданных задач.");

        int taskNumber = 0;
        for (Task getTask : manager.getTaskList()) {
            taskNumber += 1;
            System.out.println("Задача № " + taskNumber + ".");
            System.out.println("Название: " + getTask.name + ".");
            System.out.println("Описание: " + getTask.description + ".");
            System.out.println("Статус: " + getTask.status + ".");
        }
        System.out.println(System.lineSeparator());

        int epicNumber = 0;
        for (Epic getEpic : manager.getEpicList()) {
            epicNumber += 1;
            System.out.println("Эпик № " + epicNumber + ".");
            System.out.println("Название: " + getEpic.name + ".");
            System.out.println("Описание: " + getEpic.description + ".");
            System.out.println("Ниже выводим номера подзадач: ");
            for (int getSubTasksId : getEpic.subTasksId) {
                System.out.println(getSubTasksId);
            }
            System.out.println("Статус: " + getEpic.status + ".");
        }
        System.out.println(System.lineSeparator());

        int subtaskNumber = 0;
        for (Subtask getSubtask : manager.getSubtaskList()) {
            subtaskNumber += 1;
            System.out.println("Подзадача № " + subtaskNumber + ".");
            System.out.println("Название: " + getSubtask.name + ".");
            System.out.println("Описание: " + getSubtask.description + ".");
            System.out.println("Подзадача относится к эпику с id " + getSubtask.epicId + ".");
            System.out.println("Статус: " + getSubtask.status + ".");
        }
        System.out.println(System.lineSeparator());
        System.out.println("Меняем статусы созданных объектов.");

        task = new Task("Пойти спать", "почему-то всегда забываю", "IN_PROGRESS");
        manager.updateTask(1, task);

        task = new Task("Оплатить YouTube премиум", "теперь это целый квест", "DONE");
        manager.updateTask(2, task);

        subtask = new Subtask("Почесать за правым ухом", "чесать нужно медленно",
                "DONE", 3);
        manager.updateSubtask(4, subtask, manager);

        subtask = new Subtask("Почесать животик", "там он не достает, поэтому забавно кайфует",
                "DONE", 3);
        manager.updateSubtask(5, subtask, manager);

        subtask = new Subtask("Починить микроволновку", "жена сказала, что программисты это умеют",
                "IN_PROGRESS", 6);
        manager.updateSubtask(7, subtask, manager);
        System.out.println("*СТАТУСЫ ПОДЗАДАЧ ИЗМЕНЕНЫ*");
        System.out.println(System.lineSeparator());

        System.out.println("Печатаем новые статусы");

        for (Task getTask : manager.getTaskList()) {
            System.out.println("Статус задачи " + getTask.name + " изменился на " + getTask.status + ".");
        }
        System.out.println(System.lineSeparator());

        for (Epic getEpic : manager.getEpicList()) {
            System.out.println("Статус задачи " + getEpic.name + " изменился на " + getEpic.status + ".");
        }
        System.out.println(System.lineSeparator());

        for (Subtask getSubtask : manager.getSubtaskList()) {
            System.out.println("Статус задачи " + getSubtask.name + " изменился на " + getSubtask.status + ".");
        }
        System.out.println(System.lineSeparator());
        System.out.println("И, наконец, удаляем 1 задачу и 1 эпик");

        manager.removeTask(2);
        manager.removeEpic(3);
        System.out.println("*ЗАДАЧИ УДАЛЕНЫ*");
        System.out.println(System.lineSeparator());
        System.out.println("Теперь задачи выглядят вот так:");

        taskNumber = 0;
        for (Task getTask : manager.getTaskList()) {
            taskNumber += 1;
            System.out.println("Задача № " + taskNumber + ".");
            System.out.println("Название: " + getTask.name + ".");
            System.out.println("Описание: " + getTask.description + ".");
            System.out.println("Статус: " + getTask.status + ".");
        }
        System.out.println(System.lineSeparator());

        epicNumber = 0;
        for (Epic getEpic : manager.getEpicList()) {
            epicNumber += 1;
            System.out.println("Эпик № " + epicNumber + ".");
            System.out.println("Название: " + getEpic.name + ".");
            System.out.println("Описание: " + getEpic.description + ".");
            System.out.println("Ниже выводим номера подзадач: ");
            for (int getSubTasksId : getEpic.subTasksId) {
                System.out.println(getSubTasksId);
            }
            System.out.println("Статус: " + getEpic.status + ".");
        }
        System.out.println(System.lineSeparator());

        subtaskNumber = 0;
        for (Subtask getSubtask : manager.getSubtaskList()) {
            subtaskNumber += 1;
            System.out.println("Подзадача № " + subtaskNumber + ".");
            System.out.println("Название: " + getSubtask.name + ".");
            System.out.println("Описание: " + getSubtask.description + ".");
            System.out.println("Подзадача относится к эпику с id " + getSubtask.epicId + ".");
            System.out.println("Статус: " + getSubtask.status + ".");
        }
    }
}

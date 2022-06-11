public class Main {

    public static void main(String[] args) {
        Manager manager = new Manager();

        /*Создание объекта я все-таки предлагаю оставить в main, как и сказано в ТЗ
        это устранит вероятность появления проблем с дальнейшей работой программы по ТЗ следующих спринтов*/

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
        manager.getEpicById(3).recordSubtasks(subtask, manager);

        subtask = new Subtask("Почесать животик",
                "там он не достает, поэтому забавно кайфует",3);
        manager.getEpicById(3).recordSubtasks(subtask, manager);

        epic = new Epic("Стать программистом", "зря я 8 лет строил карьеру в SMM");
        manager.recordEpics(epic);

        subtask = new Subtask("Починить микроволновку",
                "жена сказала, что программисты это умеют",6);
        manager.getEpicById(6).recordSubtasks(subtask, manager);

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
        manager.updateTask(1, task, "IN_PROGRESS");

        task = new Task("Оплатить YouTube премиум", "теперь это целый квест");
        manager.updateTask(2, task, "DONE");

        subtask = new Subtask("Почесать за правым ухом", "чесать нужно медленно", 3);
        manager.updateSubtask(4, subtask, "DONE");

        subtask = new Subtask("Почесать животик",
                "там он не достает, поэтому забавно кайфует", 3);
        manager.updateSubtask(5, subtask, "DONE");

        subtask = new Subtask("Починить микроволновку",
                "жена сказала, что программисты это умеют", 6);
        manager.updateSubtask(7, subtask, "IN_PROGRESS");
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
    }
}

package test;

import main.Exceptions.ManagerSaveException;
import main.Httpserver.KVServer;
import main.taskManagement.TaskManager;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;
import main.utils.TaskStatuses;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.assertTrue;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    abstract T createTestManager();
    protected KVServer kvServer;

    @BeforeEach
    protected void initTasks() throws ManagerSaveException, IOException {
        kvServer = new KVServer();
        kvServer.start();
        taskManager = createTestManager();
        taskManager.recordTasks(new Task("Задача №1", "Описание задачи №1",
                LocalDateTime.of(2025, 9, 12, 10, 0), 30));
        taskManager.recordTasks(new Task("Задача №2", "Описание задачи №2",
                LocalDateTime.of(2024, 9, 12, 14, 20), 360));
        Epic epic1 = new Epic("Эпик №1 с тремя подзадачами", "Описание эпика №1");
        taskManager.recordEpics(epic1);
        taskManager.recordSubtasks((new Subtask("Подзадача №1 эпика №1",
                "описание подзадачи №1 эпика №1",
                LocalDateTime.of(2023, 9, 1, 8, 0),
                60, epic1.getId())), epic1.getId());
        taskManager.recordSubtasks((new Subtask("Подзадача №2 эпика №1",
                "описание подзадачи №2 эпика №1",
                LocalDateTime.of(2022, 10, 13, 8, 0),
                20, epic1.getId())), epic1.getId());
        taskManager.recordSubtasks((new Subtask("Подзадача №3 эпика №1",
                "описание подзадачи №3 эпика №1",
                LocalDateTime.of(2021, 11, 20, 8, 0),
                45, epic1.getId())), epic1.getId());
        taskManager.recordEpics(new Epic("Эпик №2 без подзадач", "Описание эпика №2"));
    }

    @AfterEach
    protected void clearTaskLists() throws ManagerSaveException {
        taskManager.removeTasks();
        taskManager.removeEpics();
        Task.setGenId(0);    // обнуляем генерируемый id, чтобы статический счетчик начал отсчет заново
        kvServer.stop();
    }

    @Test
    @DisplayName("Тест записи задачи")
    public void recordTasksTest() throws ManagerSaveException {
        Task task = new Task("Тестовая задача", "Описание тестовой задачи",
                LocalDateTime.of(2022, 1, 1, 10, 0), 30);

        taskManager.recordTasks(task);
        assertTrue(taskManager.getTaskList().contains(task));

        assertThrows(NullPointerException.class, () -> taskManager.recordTasks(null));
    }

    @Test
    @DisplayName("Тест записи эпика")
    public void recordEpicsTest() throws ManagerSaveException {
        Epic epic = new Epic("Тестовый эпик", "Описание тестового эпика");

        taskManager.recordEpics(epic);
        assertTrue(taskManager.getEpicList().contains(epic));

        assertThrows(NullPointerException.class, () -> taskManager.recordEpics(null));
    }

    @Test
    @DisplayName("Тест записи подзадачи")
    public void recordSubtasksTest() throws ManagerSaveException {
        Epic epic3 = new Epic("Эпик №3", "Для теста записи подзадачи");
        taskManager.recordEpics(epic3);
        Subtask subtask = new Subtask("Подзадача тестового эпика",
                "описание подзадачи тестового эпика",
                LocalDateTime.of(2022, 11, 15, 2, 3), 45, epic3.getId());

        assertThrows(NullPointerException.class, () -> taskManager.recordSubtasks(subtask, -1));

        taskManager.recordSubtasks(subtask, epic3.getId());
        assertTrue(taskManager.getSubtaskList().contains(subtask));

        assertThrows(NullPointerException.class, () -> taskManager.recordSubtasks(null, epic3.getId()));
    }

    @Test
    @DisplayName("Тест получения задач")
    public void getTaskListTest() throws ManagerSaveException {
        assertEquals(2, taskManager.getTaskList().size());

        taskManager.removeTasks();
        assertEquals(0, taskManager.getTaskList().size());
    }

    @Test
    @DisplayName("Тест получения эпиков")
    public void getEpicListTest() throws ManagerSaveException {
        assertEquals(2, taskManager.getEpicList().size());

        taskManager.removeEpics();
        assertEquals(0, taskManager.getEpicList().size());
    }

    @Test
    @DisplayName("Тест получения подзадач")
    public void getSubtaskListTest() throws ManagerSaveException {
        assertEquals(3, taskManager.getSubtaskList().size());

        taskManager.removeEpics();
        assertEquals(0, taskManager.getSubtaskList().size());
    }

    @Test
    @DisplayName("Тест удаления задач")
    public void removeTasksTest() throws ManagerSaveException {
        taskManager.removeTasks();
        assertEquals(0, taskManager.getTaskList().size());

        assertThrows(IndexOutOfBoundsException.class, () -> taskManager.getTaskList().get(0));
    }

    @Test
    @DisplayName("Тест удаления эпиков")
    public void removeEpicsTest() throws ManagerSaveException {
        taskManager.removeEpics();
        assertEquals(0, taskManager.getEpicList().size());

        assertThrows(IndexOutOfBoundsException.class, () -> taskManager.getEpicList().get(0));
    }

    @Test
    @DisplayName("Тест удаления подзадач")
    public void removeSubtasksTest() throws ManagerSaveException {
        taskManager.removeSubtasks();
        assertEquals(0, taskManager.getSubtaskList().size());

        assertThrows(IndexOutOfBoundsException.class, () -> taskManager.getSubtaskList().get(0));
    }

    @Test
    @DisplayName("Тест получения задачи по id")
    public void getTaskByIdTest() throws ManagerSaveException {
        Task task = new Task("Тестовая задача", "Описание тестовой задачи",
                LocalDateTime.of(2022, 1, 1, 10, 0), 30);

        taskManager.recordTasks(task);
        assertEquals(task, taskManager.getTaskById(8));

        assertNull(taskManager.getTaskById(-1));

        taskManager.removeTasks();
        assertNull(taskManager.getTaskById(8));
    }

    @Test
    @DisplayName("Тест получения эпика по id")
    public void getEpicByIdTest() throws ManagerSaveException {
        Epic epic = new Epic("Тестовый эпик", "Описание тестового эпика");

        taskManager.recordEpics(epic);
        assertEquals(epic, taskManager.getEpicById(8));

        assertNull(taskManager.getEpicById(-1));

        taskManager.removeEpics();
        assertNull(taskManager.getEpicById(8));
    }

    @Test
    @DisplayName("Тест получения подзадачи по id")
    public void getSubtaskByIdTest() throws ManagerSaveException {
        Subtask subtask = new Subtask("Подзадача тестового эпика",
                "описание подзадачи тестового эпика",
                LocalDateTime.of(2022, 11, 15, 2, 3), 45, 3);

        taskManager.recordSubtasks(subtask, 3);
        assertEquals(subtask, taskManager.getSubtaskById(8));

        assertThrows(NullPointerException.class, () -> taskManager.getSubtaskById(-1));

        taskManager.removeSubtasks();
        assertThrows(NullPointerException.class, () -> taskManager.getSubtaskById(8));
    }

    @Test
    @DisplayName("Тест обновления задачи")
    public void updateTaskTest() throws ManagerSaveException {
        Task task = new Task("Тестовая задача", "Описание тестовой задачи",
                LocalDateTime.of(2022, 1, 1, 10, 0), 30);
        taskManager.updateTask(1, task, TaskStatuses.IN_PROGRESS);
        assertEquals(task, taskManager.getTaskById(1));

        assertThrows(NullPointerException.class, () -> taskManager.updateTask(-1, null, null));

        taskManager.removeTasks();
        taskManager.updateTask(1, task, TaskStatuses.IN_PROGRESS);
        assertEquals(task, taskManager.getTaskById(1));
    }

    @Test
    @DisplayName("Тест обновления эпика")
    public void updateEpicTest() throws ManagerSaveException {
        Epic epic = new Epic("Тестовый эпик", "Описание тестового эпика");
        taskManager.updateEpic(3, epic);
        assertEquals(epic, taskManager.getEpicById(3));

        assertThrows(NullPointerException.class, () -> taskManager.updateEpic(-1, null));

        taskManager.removeEpics();
        taskManager.updateEpic(3, epic);
        assertEquals(epic, taskManager.getEpicById(3));
    }

    @Test
    @DisplayName("Тест обновления подзадачи")
    public void updateSubtaskTest() throws ManagerSaveException {
        Subtask subtask = new Subtask("Подзадача тестового эпика",
                "описание подзадачи тестового эпика",
                LocalDateTime.of(2022, 11, 15, 2, 3), 45, 3);
        taskManager.updateSubtask(4, subtask, TaskStatuses.IN_PROGRESS);
        assertEquals(subtask, taskManager.getSubtaskById(4));

        assertThrows(NullPointerException.class, () -> taskManager.updateSubtask(-1, null, null));

        taskManager.removeSubtasks();
        taskManager.updateSubtask(4, subtask, TaskStatuses.IN_PROGRESS);
        assertEquals(subtask, taskManager.getSubtaskById(4));
    }

    @Test
    @DisplayName("Тест удаления задачи")
    public void removeTaskTest() throws ManagerSaveException {
        taskManager.removeTask(1);
        assertNull(taskManager.getTaskById(1));

        assertNull(taskManager.getTaskById(-1));

        taskManager.removeTasks();
        assertNull(taskManager.getTaskById(2));
    }

    @Test
    @DisplayName("Тест удаления эпика")
    public void removeEpicTest() throws ManagerSaveException {
        taskManager.removeEpic(3);
        assertNull(taskManager.getEpicById(3));

        assertNull(taskManager.getEpicById(-1));

        taskManager.removeEpics();
        assertNull(taskManager.getEpicById(7));
    }

    @Test
    @DisplayName("Тест удаления подзадачи")
    public void removeSubtaskTest() throws ManagerSaveException {
        taskManager.removeSubtask(4);
        assertThrows(NullPointerException.class, () -> taskManager.getSubtaskById(4));

        assertThrows(NullPointerException.class, () -> taskManager.getSubtaskById(-1));

        taskManager.removeSubtasks();
        assertThrows(NullPointerException.class, () -> taskManager.getSubtaskById(5));
    }

    @Test
    @DisplayName("Тест получения подзадач эпика")
    public void getEpicIdSubtasksTest() throws ManagerSaveException {
        assertEquals(taskManager.getSubtaskList(), taskManager.getEpicIdSubtasks(3));

        assertThrows(NullPointerException.class, () -> taskManager.getEpicIdSubtasks(-1));

        taskManager.removeSubtasks();
        ArrayList<Subtask> epicIdSubtasks = new ArrayList<>();
        assertEquals(epicIdSubtasks, taskManager.getEpicIdSubtasks(3));
    }

    @Test
    @DisplayName("Тест получения списка истории задач")
    public void getHistoryTest() throws ManagerSaveException {
        List<Integer> resultHistoryId = new ArrayList<>();
        List<Integer> correctHistoryId = List.of(1, 2, 6);
        taskManager.getTaskById(2);
        taskManager.getSubtaskById(6);
        taskManager.getTaskById(1);
        taskManager.getTaskById(2);
        taskManager.getTaskById(1);
        for (Task task : taskManager.getHistory()) {
            resultHistoryId.add(task.getId());
        }
        assertEquals(correctHistoryId, resultHistoryId);

        taskManager.removeTasks();
        taskManager.removeEpics();
        assertEquals(new ArrayList<>(), taskManager.getHistory());
    }

    @Test
    @DisplayName("Проверяем наличие эпика у подзадач")
    public void checkingEpicInSubtasksTest() throws ManagerSaveException {
        for (Subtask subtask : taskManager.getSubtaskList()) {
            assertTrue(taskManager.getEpicList().contains(taskManager.getEpicById(subtask.getEpicId())));
            assertFalse(taskManager.getEpicList().contains(taskManager.getEpicById(subtask.getEpicId() * (-1))));
        }
    }

    @Test
    @DisplayName("Проверяем расчет статуса эпика")
    public void checkingComputationEpicStatusTest() throws ManagerSaveException {
        Epic epic = new Epic("Тестовый эпик", "Описание тестового эпика");
        taskManager.recordEpics(epic);
        assertEquals(TaskStatuses.NEW, epic.getStatus()); //расчет без подзадач

        Subtask subtask1 = new Subtask("Подзадача тестового эпика 1",
                "описание подзадачи тестового эпика",
                LocalDateTime.of(2022, 11, 15, 2, 3), 45, 8);
        Subtask subtask2 = new Subtask("Подзадача тестового эпика 2",
                "описание подзадачи тестового эпика",
                LocalDateTime.of(2022, 6, 15, 2, 3), 45, 8);
        assertEquals(TaskStatuses.NEW, epic.getStatus()); //расчет с NEW подзадачами

        taskManager.updateSubtask(10, subtask2, TaskStatuses.DONE);
        assertEquals(TaskStatuses.DONE, epic.getStatus()); //расчет с NEW и DONE подзадачами

        taskManager.removeSubtask(9);
        assertEquals(TaskStatuses.DONE, epic.getStatus()); //расчет с DONE подзадачами

        Subtask subtask3 = new Subtask("Подзадача тестового эпика 1",
                "описание подзадачи тестового эпика",
                LocalDateTime.of(2022, 4, 15, 2, 3), 45, 8);
        taskManager.updateSubtask(11, subtask3, TaskStatuses.IN_PROGRESS);
        taskManager.updateSubtask(10, new Subtask("Подзадача тестового эпика 2",
                "описание подзадачи тестового эпика",
                LocalDateTime.of(2022, 8, 15, 2, 3), 45, 8),
                TaskStatuses.IN_PROGRESS);
        assertEquals(TaskStatuses.IN_PROGRESS, epic.getStatus()); //расчет с IN_PROGRESS подзадачами
    }
}
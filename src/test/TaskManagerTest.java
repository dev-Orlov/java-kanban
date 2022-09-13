package test;

import main.Exceptions.ManagerSaveException;
import main.taskManagement.TaskManager;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;
import org.junit.jupiter.api.*;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.*;

abstract class TaskManagerTest<T extends TaskManager> {

    protected T taskManager;

    abstract T createTestManager();

    @BeforeEach
    protected void initTasks() throws ManagerSaveException {
        taskManager = createTestManager();
        taskManager.recordTasks(new Task("Задача №1", "Описание задачи №1",
                LocalDateTime.of(2025, 9, 12, 10, 0), 30));
        taskManager.recordTasks(new Task("Задача №2", "Описание задачи №2",
                LocalDateTime.of(2024, 9, 12, 14, 20), 360));
        taskManager.recordEpics(new Epic("Эпик №1 с тремя подзадачами", "Описание эпика №1"));
        taskManager.recordSubtasks((new Subtask("Подзадача №1 эпика №1",
                "описание подзадачи №1 эпика №1",
                LocalDateTime.of(2023, 9, 1, 8, 0), 60, 3)), 3);
        taskManager.recordSubtasks((new Subtask("Подзадача №2 эпика №1",
                "описание подзадачи №2 эпика №1",
                LocalDateTime.of(2022, 10, 13, 8, 0), 20, 3)), 3);
        taskManager.recordSubtasks((new Subtask("Подзадача №3 эпика №1",
                "описание подзадачи №3 эпика №1",
                LocalDateTime.of(2021, 11, 20, 8, 0), 45, 3)), 3);
        taskManager.recordEpics(new Epic("Эпик №2 без подзадач", "Описание эпика №2"));
    }

    @AfterEach
    protected void clearTaskLists() throws ManagerSaveException {
        taskManager.removeTasks();
        taskManager.removeEpics();
        Task.setGenId(0);    // обнуляем генерируемый id, чтобы статический счетчик начал отсчет заново
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
        Subtask subtask = new Subtask("Подзадача тестового эпика",
                "описание подзадачи тестового эпика",
                LocalDateTime.of(2022, 11, 15, 2, 3), 45, 3);

        assertThrows(NullPointerException.class, () -> taskManager.recordSubtasks(subtask, -1));

        taskManager.recordSubtasks(subtask, 3);
        assertTrue(taskManager.getSubtaskList().contains(subtask));

        assertThrows(NullPointerException.class, () -> taskManager.recordSubtasks(null, 3));
    }

    @Test
    @DisplayName("Тест получения задач")
    public void getTaskListTest() throws ManagerSaveException {
        assertEquals(taskManager.getTaskList().size(), 2);

        taskManager.removeTasks();
        assertEquals(taskManager.getTaskList().size(), 0);
    }

    @Test
    @DisplayName("Тест получения эпиков")
    public void getEpicListTest() throws ManagerSaveException {
        assertEquals(taskManager.getEpicList().size(), 2);

        taskManager.removeEpics();
        assertEquals(taskManager.getEpicList().size(), 0);
    }

    @Test
    @DisplayName("Тест получения подзадач")
    public void getSubtaskListTest() throws ManagerSaveException {
        assertEquals(taskManager.getSubtaskList().size(), 3);

        taskManager.removeEpics();
        assertEquals(taskManager.getSubtaskList().size(), 0);
    }

    @Test
    @DisplayName("Тест удаления задач")
    public void removeTasksTest() throws ManagerSaveException {
        taskManager.removeTasks();
        assertEquals(taskManager.getTaskList().size(), 0);

        assertThrows(IndexOutOfBoundsException.class, () -> taskManager.getTaskList().get(0));
    }

    @Test
    @DisplayName("Тест удаления эпиков")
    public void removeEpicsTest() throws ManagerSaveException {
        taskManager.removeEpics();
        assertEquals(taskManager.getEpicList().size(), 0);

        assertThrows(IndexOutOfBoundsException.class, () -> taskManager.getEpicList().get(0));
    }

    @Test
    @DisplayName("Тест удаления подзадач")
    public void removeSubtasksTest() throws ManagerSaveException {
        taskManager.removeSubtasks();
        assertEquals(taskManager.getSubtaskList().size(), 0);

        assertThrows(IndexOutOfBoundsException.class, () -> taskManager.getSubtaskList().get(0));
    }

    @Test
    @DisplayName("Тест получения задачи по id")
    public void getTaskByIdTest() throws ManagerSaveException {
        Task task = new Task("Тестовая задача", "Описание тестовой задачи",
                LocalDateTime.of(2022, 1, 1, 10, 0), 30);

        taskManager.recordTasks(task);
        assertEquals(taskManager.getTaskById(8), task);

        assertNull(taskManager.getTaskById(-1));

        taskManager.removeTasks();
        assertNull(taskManager.getTaskById(8));
    }

    @Test
    @DisplayName("Тест получения эпика по id")
    public void getEpicByIdTest() throws ManagerSaveException {
        Epic epic = new Epic("Тестовый эпик", "Описание тестового эпика");

        taskManager.recordEpics(epic);
        assertEquals(taskManager.getEpicById(8), epic);

        assertNull(taskManager.getEpicById(-1));

        taskManager.removeEpics();
        assertNull(taskManager.getEpicById(8));
    }

    @Test
    @DisplayName("Тест получения подзадачи по id")
    public void getSubtaskByIdTest() throws ManagerSaveException {
        Epic epic = new Epic("Тестовый эпик", "Описание тестового эпика");

        taskManager.recordEpics(epic);
        assertEquals(taskManager.getEpicById(8), epic);

        assertNull(taskManager.getEpicById(-1));

        taskManager.removeEpics();
        assertNull(taskManager.getEpicById(8));
    }
}
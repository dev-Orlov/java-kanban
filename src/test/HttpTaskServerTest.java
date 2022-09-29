package test;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import main.exceptions.ManagerSaveException;
import main.httpserver.HttpTaskServer;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;
import org.junit.jupiter.api.*;

import java.io.IOException;
import java.lang.reflect.Type;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HttpTaskServerTest {

    private HttpTaskServer server;
    private final Gson gson = new Gson();

    private final Type taskType = new TypeToken<ArrayList<Epic>>(){}.getType();
    private final Type epicType = new TypeToken<ArrayList<Epic>>(){}.getType();
    private final Type subtaskType = new TypeToken<ArrayList<Subtask>>(){}.getType();

    @BeforeEach
    protected void initTasks() throws ManagerSaveException, IOException {
        server = new HttpTaskServer();
        server.startServer();

        postRequest((new Task("Задача №1", "Описание задачи №1",
                LocalDateTime.of(2025, 9, 12, 10, 0), 30)),
                "http://localhost:8080/tasks/task/");
        postRequest((new Task("Задача №2", "Описание задачи №2",
                        LocalDateTime.of(2024, 9, 12, 14, 20), 360)),
                "http://localhost:8080/tasks/task/");
        Epic epic1 = new Epic("Эпик №1 с тремя подзадачами", "Описание эпика №1");
        postRequest(epic1, "http://localhost:8080/tasks/epic/");
        postRequest((new Subtask("Подзадача №1 эпика №1",
                        "описание подзадачи №1 эпика №1",
                        LocalDateTime.of(2023, 9, 1, 8, 0),
                        60, epic1.getId())),
                "http://localhost:8080/tasks/subtask/");
        postRequest((new Subtask("Подзадача №2 эпика №1",
                        "описание подзадачи №2 эпика №1",
                        LocalDateTime.of(2022, 10, 13, 8, 0),
                        20, epic1.getId())),
                "http://localhost:8080/tasks/subtask/");
        postRequest((new Subtask("Подзадача №3 эпика №1",
                        "описание подзадачи №3 эпика №1",
                        LocalDateTime.of(2021, 11, 20, 8, 0),
                        45, epic1.getId())),
                "http://localhost:8080/tasks/subtask/");
        postRequest((new Epic("Эпик №2 без подзадач", "Описание эпика №2")),
                "http://localhost:8080/tasks/epic/");
    }

    private void postRequest(Task task, String stringURL) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            String json = gson.toJson(task);
            URI url = URI.create(stringURL);
            HttpRequest.BodyPublisher body = HttpRequest.BodyPublishers.ofString(json);
            HttpRequest request = HttpRequest.newBuilder().uri(url).POST(body).build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("При отправке POST-запроса возникла ошибка");
        }
    }

    private String getRequest(String stringURL) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(stringURL);
            HttpRequest request = HttpRequest.newBuilder().uri(url).GET().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            return response.body();
        } catch (IOException | InterruptedException e) {
            System.out.println("При отправке GET-запроса возникла ошибка");
            return "";
        }
    }

    private void deleteRequest(String stringURL) {
        try {
            HttpClient client = HttpClient.newHttpClient();
            URI url = URI.create(stringURL);
            HttpRequest request = HttpRequest.newBuilder().uri(url).DELETE().build();
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
        } catch (IOException | InterruptedException e) {
            System.out.println("При отправке DELETE-запроса возникла ошибка");
        }
    }

    @AfterEach
    protected void clearTaskLists() throws ManagerSaveException {
        deleteRequest("http://localhost:8080/tasks/task/");
        deleteRequest("http://localhost:8080/tasks/epic/");
        deleteRequest("http://localhost:8080/tasks/subtask/");
        Task.setGenId(0);    // обнуляем генерируемый id, чтобы статический счетчик начал отсчет заново
        server.stopServer();
    }

    @Test
    @DisplayName("Тест записи задачи")
    public void recordTasksTest() throws ManagerSaveException {
        Task task = new Task("Тестовая задача", "Описание тестовой задачи",
                LocalDateTime.of(2022, 1, 1, 10, 0), 30);

        postRequest(task, "http://localhost:8080/tasks/task/");
        String findTask = getRequest("http://localhost:8080/tasks/task/");
        ArrayList<Task> serializedTasks = gson.fromJson(findTask, taskType);
        assertEquals(serializedTasks.get(2).getId(), task.getId());
    }

    @Test
    @DisplayName("Тест записи эпика")
    public void recordEpicsTest() throws ManagerSaveException {
        Epic epic = new Epic("Тестовый эпик", "Описание тестового эпика");

        postRequest(epic, "http://localhost:8080/tasks/epic/");
        String findTask = getRequest("http://localhost:8080/tasks/epic/");
        ArrayList<Epic> serializedTasks = gson.fromJson(findTask, epicType);
        assertEquals(serializedTasks.get(2).getId(), epic.getId());
    }

    @Test
    @DisplayName("Тест записи подзадачи")
    public void recordSubtasksTest() throws ManagerSaveException {
        Epic epic3 = new Epic("Эпик №3", "Для теста записи подзадачи");
        postRequest(epic3, "http://localhost:8080/tasks/epic/");

        Subtask subtask = new Subtask("Подзадача тестового эпика",
                "описание подзадачи тестового эпика",
                LocalDateTime.of(2022, 11, 15, 2, 3), 45, epic3.getId());
        postRequest(subtask, "http://localhost:8080/tasks/subtask/");

        String findTask = getRequest("http://localhost:8080/tasks/subtask/");
        ArrayList<Subtask> serializedTasks = gson.fromJson(findTask, subtaskType);
        assertEquals(subtask.getId(), serializedTasks.get(3).getId());
    }

    @Test
    @DisplayName("Тест получения задач")
    public void getTaskListTest() throws ManagerSaveException {
        String findTask = getRequest("http://localhost:8080/tasks/task/");
        ArrayList<Task> serializedTasks = gson.fromJson(findTask, taskType);

        assertEquals(2, serializedTasks.size());

        deleteRequest("http://localhost:8080/tasks/task/");
        String findTask2 = getRequest("http://localhost:8080/tasks/task/");
        ArrayList<Task> serializedTasks2 = gson.fromJson(findTask2, taskType);
        assertEquals(0, serializedTasks2.size());
    }

    @Test
    @DisplayName("Тест получения эпиков")
    public void getEpicListTest() throws ManagerSaveException {
        String findTask = getRequest("http://localhost:8080/tasks/epic/");
        ArrayList<Epic> serializedTasks = gson.fromJson(findTask, epicType);

        assertEquals(2, serializedTasks.size());

        deleteRequest("http://localhost:8080/tasks/epic/");
        String findTask2 = getRequest("http://localhost:8080/tasks/epic/");
        ArrayList<Epic> serializedTasks2 = gson.fromJson(findTask2, epicType);
        assertEquals(0, serializedTasks2.size());
    }

    @Test
    @DisplayName("Тест получения подзадач")
    public void getSubtaskListTest() throws ManagerSaveException {
        String findTask = getRequest("http://localhost:8080/tasks/subtask/");
        ArrayList<Subtask> serializedTasks = gson.fromJson(findTask, subtaskType);

        assertEquals(3, serializedTasks.size());

        deleteRequest("http://localhost:8080/tasks/subtask/");
        String findTask2 = getRequest("http://localhost:8080/tasks/subtask/");
        ArrayList<Subtask> serializedTasks2 = gson.fromJson(findTask2, subtaskType);
        assertEquals(0, serializedTasks2.size());
    }

    @Test
    @DisplayName("Тест удаления задач")
    public void removeTasksTest() throws ManagerSaveException {
        deleteRequest("http://localhost:8080/tasks/task/");
        String findTask2 = getRequest("http://localhost:8080/tasks/task/");
        ArrayList<Task> serializedTasks = gson.fromJson(findTask2, taskType);
        assertEquals(0, serializedTasks.size());
    }

    @Test
    @DisplayName("Тест удаления эпиков")
    public void removeEpicsTest() throws ManagerSaveException {
        deleteRequest("http://localhost:8080/tasks/epic/");
        String findTask2 = getRequest("http://localhost:8080/tasks/epic/");
        ArrayList<Epic> serializedTasks = gson.fromJson(findTask2, epicType);
        assertEquals(0, serializedTasks.size());
    }

    @Test
    @DisplayName("Тест удаления подзадач")
    public void removeSubtasksTest() throws ManagerSaveException {
        deleteRequest("http://localhost:8080/tasks/subtask/");
        String findTask2 = getRequest("http://localhost:8080/tasks/subtask/");
        ArrayList<Subtask> serializedTasks = gson.fromJson(findTask2, subtaskType);
        assertEquals(0, serializedTasks.size());
    }

    @Test
    @DisplayName("Тест получения задачи по id")
    public void getTaskByIdTest() throws ManagerSaveException {
        Task task = new Task("Тестовая задача", "Описание тестовой задачи",
                LocalDateTime.of(2022, 1, 1, 10, 0), 30);
        postRequest(task, "http://localhost:8080/tasks/task/");
        Task findTask = gson.fromJson(getRequest("http://localhost:8080/tasks/task/?id=8"), Task.class);

        assertEquals(task.toString(), findTask.toString());
    }

    @Test
    @DisplayName("Тест получения эпика по id")
    public void getEpicByIdTest() throws ManagerSaveException {
        Epic epic = new Epic("Тестовый эпик", "Описание тестового эпика");
        postRequest(epic, "http://localhost:8080/tasks/epic/");
        Epic findTask = gson.fromJson(getRequest("http://localhost:8080/tasks/epic/?id=8"), Epic.class);

        assertEquals(epic.toString(), findTask.toString());
    }

    @Test
    @DisplayName("Тест получения подзадачи по id")
    public void getSubtaskByIdTest() throws ManagerSaveException {
        Subtask subtask = new Subtask("Подзадача тестового эпика",
                "описание подзадачи тестового эпика",
                LocalDateTime.of(2022, 11, 15, 2, 3), 45, 3);
        postRequest(subtask, "http://localhost:8080/tasks/subtask/");
        Subtask findTask = gson.fromJson(getRequest("http://localhost:8080/tasks/subtask/?id=8"),
                Subtask.class);

        assertEquals(subtask.toString(), findTask.toString());
    }

    @Test
    @DisplayName("Тест обновления задачи")
    public void updateTaskTest() throws ManagerSaveException {
        Task task = new Task("Тестовая задача", "Описание тестовой задачи",
                LocalDateTime.of(2022, 1, 1, 10, 0), 30);
        postRequest(task, "http://localhost:8080/tasks/task/?id=1");
        Task findTask = gson.fromJson(getRequest("http://localhost:8080/tasks/task/?id=1"), Task.class);

        assertEquals(task.getName(), findTask.getName());
        assertEquals(task.getStartTime(), findTask.getStartTime());
        assertEquals(task.getType(), findTask.getType());
        assertEquals(task.getDuration(), findTask.getDuration());
        assertEquals(task.getStatus(), findTask.getStatus());
    }

    @Test
    @DisplayName("Тест обновления эпика")
    public void updateEpicTest() throws ManagerSaveException {
        Epic epic = new Epic("Тестовый эпик", "Описание тестового эпика");
        postRequest(epic, "http://localhost:8080/tasks/epic/?id=3");
        Epic findTask = gson.fromJson(getRequest("http://localhost:8080/tasks/epic/?id=3"), Epic.class);

        assertEquals(epic.getName(), findTask.getName());
        assertEquals(epic.getStartTime(), findTask.getStartTime());
        assertEquals(epic.getType(), findTask.getType());
        assertEquals(epic.getDuration(), findTask.getDuration());
        assertEquals(epic.getStatus(), findTask.getStatus());
    }

    @Test
    @DisplayName("Тест обновления подзадачи")
    public void updateSubtaskTest() throws ManagerSaveException {
        /*Subtask subtask = new Subtask("Подзадача тестового эпика",
                "описание подзадачи тестового эпика",
                LocalDateTime.of(2022, 11, 15, 2, 3), 45, 3);
        postRequest(subtask, "http://localhost:8080/tasks/subtask/?id=4");
        Subtask findTask = gson.fromJson(getRequest("http://localhost:8080/tasks/subtask/?id=4"),
                Subtask.class);
        System.out.println(findTask);
        System.out.println(findTask.getName());
        System.out.println(findTask.getStartTime());*/


       /* assertEquals(subtask.getName(), findTask.getName());
        assertEquals(subtask.getStartTime(), findTask.getStartTime());
        assertEquals(subtask.getType(), findTask.getType());
        assertEquals(subtask.getDuration(), findTask.getDuration());
        assertEquals(subtask.getStatus(), findTask.getStatus());*/


    }

    @Test
    @DisplayName("Тест удаления задачи")
    public void removeTaskTest() throws ManagerSaveException {
        Task findTask2 = gson.fromJson(getRequest("http://localhost:8080/tasks/task/?id=1"), Task.class);
        deleteRequest("http://localhost:8080/tasks/task/?id=1");
        String findTask = getRequest("http://localhost:8080/tasks/task/");
        ArrayList<Task> serializedTasks = gson.fromJson(findTask, taskType);
        for (Task task : serializedTasks) {
            assertNotEquals(task.toString(), findTask2.toString());
        }
    }

    @Test
    @DisplayName("Тест удаления эпика")
    public void removeEpicTest() throws ManagerSaveException {
        Epic findTask2 = gson.fromJson(getRequest("http://localhost:8080/tasks/epic/?id=3"), Epic.class);
        deleteRequest("http://localhost:8080/tasks/epic/?id=3");
        String findTask = getRequest("http://localhost:8080/tasks/epic/");
        ArrayList<Epic> serializedTasks = gson.fromJson(findTask, epicType);
        for (Epic epic : serializedTasks) {
            assertNotEquals(epic.toString(), findTask2.toString());
        }
    }

    @Test
    @DisplayName("Тест удаления подзадачи")
    public void removeSubtaskTest() throws ManagerSaveException {
        Subtask findTask2 = gson.fromJson(getRequest("http://localhost:8080/tasks/subtask/?id=5"),
                Subtask.class);
        deleteRequest("http://localhost:8080/tasks/subtask/?id=5");
        String findTask = getRequest("http://localhost:8080/tasks/subtask/");
        ArrayList<Subtask> serializedTasks = gson.fromJson(findTask, subtaskType);
        for (Subtask subtask : serializedTasks) {
            assertNotEquals(subtask.toString(), findTask2.toString());
        }
    }

    @Test
    @DisplayName("Тест получения подзадач эпика")
    public void getEpicIdSubtasksTest() throws ManagerSaveException {
        String findTask = getRequest("http://localhost:8080/tasks/subtask/epic/?id=3");
        ArrayList<Subtask> serializedTasks = gson.fromJson(findTask, subtaskType);

        assertEquals(3, serializedTasks.size());
    }

    @Test
    @DisplayName("Тест получения списка истории задач")
    public void getHistoryTest() throws ManagerSaveException {
        Epic epic3 = new Epic("Эпик №3", "Для теста записи подзадачи");
        postRequest(epic3, "http://localhost:8080/tasks/epic/");
        Subtask subtask = new Subtask("Подзадача тестового эпика",
                "описание подзадачи тестового эпика",
                LocalDateTime.of(2022, 11, 15, 2, 3), 45, epic3.getId());
        postRequest(subtask, "http://localhost:8080/tasks/subtask/");

        List<Integer> correctHistoryId = List.of(1, 2, 9);
        List<Integer> resultHistoryId = new ArrayList<>();
        getRequest("http://localhost:8080/tasks/task/?id=2");
        getRequest("http://localhost:8080/tasks/subtask/?id=9");
        getRequest("http://localhost:8080/tasks/task/?id=1");
        getRequest("http://localhost:8080/tasks/task/?id=2");
        getRequest("http://localhost:8080/tasks/task/?id=1");

        Type historyType = new TypeToken<List<Task>>(){}.getType();
        String findHistory = getRequest("http://localhost:8080/tasks/history/");
        List<Task> history = gson.fromJson(findHistory, historyType);
        for (Task task : history) {
            resultHistoryId.add(task.getId());
        }
        assertEquals(correctHistoryId, resultHistoryId);
    }
}
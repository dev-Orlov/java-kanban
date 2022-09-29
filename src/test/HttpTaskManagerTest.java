package test;

import main.exceptions.ManagerSaveException;
import management.taskManagement.HttpTaskManager;
import main.tasks.Epic;
import main.utils.Managers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;

class HttpTaskManagerTest extends TaskManagerTest<HttpTaskManager> {

    @Override
    public HttpTaskManager createTestManager() {
        return Managers.getDefault();
    }

    @Test
    @DisplayName("Тест загрузки информации из сервера")
    public void load() throws ManagerSaveException {
        HttpTaskManager httpTaskManager1 = Managers.getDefault();
        taskManager.removeTasks();
        taskManager.removeEpics();
        httpTaskManager1.load();
        //файл с пустым списком задач
        assertEquals(0, httpTaskManager1.getTaskList().size());
        assertEquals(0, httpTaskManager1.getEpicList().size());
        assertEquals(0, httpTaskManager1.getSubtaskList().size());

        Epic epic = new Epic("Тестовый эпик", "Описание тестового эпика");
        taskManager.recordEpics(epic);
        HttpTaskManager httpTaskManager2 = Managers.getDefault();
        httpTaskManager2.load();
        //файл с эпиком без подзадач и с пустой историей
        assertEquals(0, httpTaskManager2.getTaskList().size());
        assertEquals(1, httpTaskManager2.getEpicList().size());
        assertEquals(0, httpTaskManager2.getSubtaskList().size());
        assertEquals(0, httpTaskManager2.getSubtaskList().size());
        assertEquals(new ArrayList<>(), taskManager.getHistory());
    }

}
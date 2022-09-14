package test;

import main.Exceptions.ManagerSaveException;
import main.historyManagement.HistoryManager;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;
import main.utils.Managers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class HistoryManagerTest {

    private HistoryManager historyManager;
    private Task task1;
    private Task task2;
    private Epic epic1;
    private Subtask subtask1;

    @BeforeEach
    protected void initTasks() throws ManagerSaveException {
        historyManager = Managers.getDefaultHistory();
        task1 = new Task("Тестовая задача", "Описание тестовой задачи",
                LocalDateTime.of(2022, 1, 1, 10, 0), 30);
        task2 = new Task("Задача №1", "Описание задачи №1",
                LocalDateTime.of(2025, 9, 12, 10, 0), 30);
        epic1 = new Epic("Эпик №1 с тремя подзадачами", "Описание эпика №1");
        subtask1 = new Subtask("Подзадача №1 эпика №1",
                "описание подзадачи №1 эпика №1",
                LocalDateTime.of(2023, 9, 1, 8, 0), 60, 3);

    }

    @Test
    @DisplayName("Тест добавления задачи в историю")
    public void addTest() {
        historyManager.add(null);
        assertEquals(new ArrayList<>(), historyManager.getHistory());

        historyManager.add(task1);
        historyManager.add(task1);
        historyManager.add(task1);
        assertEquals(1, historyManager.getHistory().size());
        assertEquals(task1, historyManager.getHistory().get(0));
    }

    @Test
    @DisplayName("Тест удаления задачи из истории")
    public void removeTest() {
        historyManager.remove(-1);
        assertEquals(new ArrayList<>(), historyManager.getHistory());

        historyManager.add(task1);
        historyManager.add(task1);
        historyManager.add(task1);
        historyManager.remove(task1.getId());
        assertEquals(0, historyManager.getHistory().size());

        historyManager.add(task2);
        historyManager.add(epic1);
        historyManager.add(subtask1);

        historyManager.remove(epic1.getId());
        assertEquals(List.of(subtask1, task2), historyManager.getHistory());

        historyManager.remove(task2.getId());
        assertEquals(List.of(subtask1), historyManager.getHistory());
    }

    @Test
    @DisplayName("Тест получения истории задач")
    public void getHistoryTest() {
        assertEquals(new ArrayList<>(), historyManager.getHistory());

        historyManager.add(epic1);
        historyManager.add(task2);
        historyManager.add(epic1);
        historyManager.add(epic1);
        historyManager.add(subtask1);
        assertEquals(3, historyManager.getHistory().size());
        assertEquals(List.of(subtask1, epic1, task2), historyManager.getHistory());
    }
}
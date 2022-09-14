package test;

import main.Exceptions.ManagerSaveException;
import main.taskManagement.FileBackedTasksManager;

import main.tasks.Epic;
import main.utils.Managers;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.ArrayList;


class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    File file = new File("TestFile.csv");

    @Override
    public FileBackedTasksManager createTestManager() {
        return Managers.getTestFileManager();
    }

    @AfterEach
    private void cleanFile() {
        file.delete();
    }

    @Test
    @DisplayName("Тест загрузки информации из файла")
    public void loadFromFileTest() throws ManagerSaveException {
        clearTaskLists();
        FileBackedTasksManager testManager1 = FileBackedTasksManager.loadFromFile(file);
        //файл с пустым списком задач
        assertEquals(0, testManager1.getTaskList().size());
        assertEquals(0, testManager1.getEpicList().size());
        assertEquals(0, testManager1.getSubtaskList().size());

        Epic epic = new Epic("Тестовый эпик", "Описание тестового эпика");
        taskManager.recordEpics(epic);
        FileBackedTasksManager testManager2 = FileBackedTasksManager.loadFromFile(file);
        //файл с эпиком без подзадач и с пустой историей
        assertEquals(0, testManager2.getTaskList().size());
        assertEquals(1, testManager2.getEpicList().size());
        assertEquals(0, testManager2.getSubtaskList().size());
        assertEquals(0, testManager2.getSubtaskList().size());
        assertEquals(new ArrayList<>(), taskManager.getHistory());
    }
}
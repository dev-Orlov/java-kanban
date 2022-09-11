package test;

import main.Exceptions.ManagerSaveException;
import main.taskManagement.FileBackedTasksManager;
import main.taskManagement.InMemoryTaskManager;
import main.tasks.Task;
import main.tasks.TasksType;
import main.utils.Managers;
import main.utils.TaskStatuses;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @BeforeEach
    public void beforeEach() {
        taskManager = Managers.getFileManager();
    }

    @Test
    @DisplayName("Запись задач")
    void recordTasksTest() throws ManagerSaveException {
        Task task = createAndRecordTask();

        final List<Task> tasks = taskManager.getTaskList();
        assertEquals(1,  tasks.size(), "Неверное количество задач.");
        assertEquals(TaskStatuses.NEW,  task.getStatus(), "Неверный статус.");
        assertEquals("Задача №1",  task.getName(), "Неверное название задачи");
        assertEquals(TasksType.TASK,  task.getType(), "Неверный тип задачи");
    }

}
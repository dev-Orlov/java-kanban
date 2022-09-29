package test;

import management.taskManagement.InMemoryTaskManager;
import main.utils.Managers;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    public InMemoryTaskManager createTestManager() {
        return Managers.getMemoryManager();
    }
}

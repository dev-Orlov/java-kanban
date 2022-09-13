package test;

import main.taskManagement.InMemoryTaskManager;
import main.utils.Managers;


class InMemoryTaskManagerTest extends TaskManagerTest<InMemoryTaskManager> {

    @Override
    public InMemoryTaskManager createTestManager() {
        return Managers.getDefault();
    }
}

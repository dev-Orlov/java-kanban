package test;

import main.taskManagement.FileBackedTasksManager;

import main.utils.Managers;


class FileBackedTasksManagerTest extends TaskManagerTest<FileBackedTasksManager> {

    @Override
    public FileBackedTasksManager createTestManager() {
        return Managers.getTestFileManager();
    }



}
package main.taskManagement;

import main.Exceptions.ManagerSaveException;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;
import main.tasks.TasksType;
import main.utils.CSVParser;
import main.utils.Managers;
import main.utils.TaskStatuses;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.LocalDateTime;
import java.util.List;

public class FileBackedTasksManager extends InMemoryTaskManager {

    private final Path autoSaveFile;

    public FileBackedTasksManager(Path autoSaveFile) {
        this.autoSaveFile = autoSaveFile;
    }

    protected void save() throws ManagerSaveException {
        try (final BufferedWriter bw = new BufferedWriter(new FileWriter(autoSaveFile.getFileName().toString()))) {
            bw.write("id,type,name,status,description, duration, startTime, endTime, epic\n");
            for (Task task : getTaskList()) { // сохранем в буфер все таски
                bw.write(task.toString() + "\n");
            }
            for (Epic epic : getEpicList()) { // сохранем в буфер все эпики
                bw.write(epic.toString() + "\n");
                for (Subtask subtask : getEpicIdSubtasks(epic.getId())) { // сохранем в буфер все сабтаски эпика
                    bw.write(subtask.toString() + "\n");
                }
            }
            bw.newLine();
            bw.write(CSVParser.historyToString(taskHistory));
            bw.flush();
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка сохранения файла");
        }
    }

    public static FileBackedTasksManager loadFromFile(File file) throws ManagerSaveException {
        try  {
            FileBackedTasksManager newManager = new FileBackedTasksManager(Paths.get(file.getCanonicalPath()));
            String oldFile = Files.readString(Path.of(file.getName()));
            String[] lines = oldFile.split("\\r?\\n");  // разбили строку файла по символу переноса
            for (int i = 1; i < lines.length; i++) {
                if (lines[i].length() > 0) {  // проверяем, не пустая ли строка
                    if (CSVParser.fromString(lines[i].trim()).getType() == TasksType.TASK) {
                        newManager.recordTasks(CSVParser.fromString(lines[i].trim()));
                    } else if (CSVParser.fromString(lines[i].trim()).getType() == TasksType.EPIC) {
                        newManager.recordEpics((Epic) CSVParser.fromString(lines[i].trim()));
                    } else if (CSVParser.fromString(lines[i].trim()).getType() == TasksType.SUBTASK) {
                        newManager.recordSubtasks((Subtask) CSVParser.fromString(lines[i].trim()),
                                ((Subtask) CSVParser.fromString(lines[i])).getEpicId());
                    }
                } else {
                    List<Integer> readHistory = CSVParser.historyFromString(lines[i+1].trim());
                    int historySize = readHistory.size();

                    for (int y = historySize - 1; y >= 0; y--) { // идем от последней задачи к первой
                        if (newManager.tasks.containsKey(readHistory.get(y))) {
                            newManager.getTaskById(readHistory.get(y));
                        } else if (newManager.epics.containsKey(readHistory.get(y))) {
                            newManager.getEpicById(readHistory.get(y));
                        } else {
                            for (int findEpic : newManager.epics.keySet()) {  // ищем нужный эпик для записи сабтаска
                                if (newManager.epics.get(findEpic).getSubtasks().containsKey(readHistory.get(y))) {
                                    newManager.getSubtaskById(readHistory.get(y));
                                }
                            }
                        }
                    }
                    break;
                }
            }
            return newManager;
        } catch (IOException e) {
            throw new ManagerSaveException("Ошибка чтения файла");
        }
    }

    @Override
    public void recordTasks(Task task) throws ManagerSaveException {
        super.recordTasks(task);
        save();
    }

    @Override
    public void recordEpics(Epic epic) throws ManagerSaveException {
        super.recordEpics(epic);
        save();
    }

    @Override
    public void recordSubtasks(Subtask subtask, int epicId) throws ManagerSaveException {
        super.recordSubtasks(subtask, epicId);
        save();
    }

    @Override
    public void removeTasks() throws ManagerSaveException {
        super.removeTasks();
        save();
    }

    @Override
    public void removeEpics() throws ManagerSaveException {
        super.removeEpics();
        save();
    }

    @Override
    public void removeSubtasks() throws ManagerSaveException {
        super.removeSubtasks();
        save();
    }

    @Override
    public Task getTaskById(int id) throws ManagerSaveException {
        super.getTaskById(id);
        save();
        return super.getTaskById(id);
    }

    @Override
    public Epic getEpicById(int id) throws ManagerSaveException {
        super.getEpicById(id);
        save();
        return super.getEpicById(id);
    }

    @Override
    public Subtask getSubtaskById(int id) throws ManagerSaveException {
        super.getSubtaskById(id);
        save();
        return super.getSubtaskById(id);
    }

    @Override
    public void updateTask(int id, Task task, TaskStatuses status) throws ManagerSaveException {
        super.updateTask(id, task, status);
        save();
    }

    @Override
    public void updateEpic(int id, Epic epic) throws ManagerSaveException {
        super.updateEpic(id, epic);
        save();
    }

    @Override
    public void updateSubtask(int id, Subtask subtask, TaskStatuses status) throws ManagerSaveException {
        super.updateSubtask(id, subtask, status);
        save();
    }

    @Override
    public void removeTask(int id) throws ManagerSaveException {
        super.removeTask(id);
        save();
    }

    @Override
    public void removeEpic(int id) throws ManagerSaveException {
        super.removeEpic(id);
        save();
    }

    @Override
    public void removeSubtask(int id) throws ManagerSaveException {
        super.removeSubtask(id);
        save();
    }

    public static void main(String[] args) throws ManagerSaveException {
        TaskManager manager = Managers.getFileManager();

        manager.recordTasks(new Task("Задача №1", "Описание задачи №1",
                LocalDateTime.of(2025, 9, 12, 10, 0), 30));
        manager.recordTasks(new Task("Задача №2", "Описание задачи №2",
                LocalDateTime.of(2024, 9, 12, 14, 20), 360));
        manager.recordEpics(new Epic("Эпик №1 с тремя подзадачами", "Описание эпика №1"));
        manager.recordSubtasks((new Subtask("Подзадача №1 эпика №1",
                "описание подзадачи №1 эпика №1",
                LocalDateTime.of(2023, 9, 1, 8, 0), 60, 3)), 3);
        manager.recordSubtasks((new Subtask("Подзадача №2 эпика №1",
                "описание подзадачи №2 эпика №1",
                LocalDateTime.of(2022, 10, 13, 8, 0), 20, 3)), 3);
        manager.recordSubtasks((new Subtask("Подзадача №3 эпика №1",
                "описание подзадачи №3 эпика №1",
                LocalDateTime.of(2021, 11, 20, 8, 0), 45, 3)), 3);
        manager.recordEpics(new Epic("Эпик №2 без подзадач", "Описание эпика №2"));

        // Запрашиваем задачи, чтобы заполнилась история просмотра

        manager.getTaskById(2);
        manager.getSubtaskById(6);
        manager.getTaskById(1);
        manager.getTaskById(2);
        manager.getTaskById(1);
        manager.getEpicById(3);
        manager.getTaskById(2);
        manager.getEpicById(7);
        manager.getSubtaskById(4);
        manager.getTaskById(1);
        manager.getEpicById(7);
        manager.getSubtaskById(5);

        loadFromFile(new File ("AutoSaveFile.csv"));
    }
}

package main.utils;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import main.Exceptions.ManagerSaveException;
import main.taskManagement.TaskManager;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final TaskManager manager = Managers.getFileManager();

    public static void main(String[] args) throws IOException, ManagerSaveException {
        HttpServer httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress( PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
        Epic epic4 = new Epic("Эпик №1 с тремя подзадачами", "Описание эпика №1");
        epic4.setId(3);
        manager.recordEpics(epic4);
        Subtask subtask2 = new Subtask("Подзадача №1 эпика №1",
                "описание подзадачи №1 эпика №1",
                LocalDateTime.of(2023, 9, 1, 8, 0), 60, 3);
        manager.recordSubtasks(subtask2, 3);
        Task task4 = new Task("Задача №1", "Описание задачи №1",
                LocalDateTime.of(2025, 9, 12, 10, 0), 30);
        manager.recordTasks(task4);
        Gson gson6 = new Gson();                                                                // тестовый код удалить!!!!!!!
        manager.getEpicById(1);
        manager.getTaskById(3);
    }

    static class TasksHandler implements HttpHandler {
        @Override
        public void handle(HttpExchange httpExchange) throws IOException {
            System.out.println("Началась обработка /tasks запроса от клиента.");

            int id = -1;
            String url = httpExchange.getRequestURI().toString();
            String method = httpExchange.getRequestMethod();
            String response = "";
            String[] splitString = httpExchange.getRequestURI().getPath().split("/");
            Gson gson = new Gson();

            int indexForGetId = url.indexOf("=");
            if (indexForGetId > 0) {
                id = Integer.parseInt(url.substring(indexForGetId + 1, url.length()));
            }

            InputStream inputStream = httpExchange.getRequestBody();
            String body = new String(inputStream.readAllBytes(), DEFAULT_CHARSET);

            switch (method) {
                case "POST":
                    if (splitString[2].equals("task")) {
                        Task task = gson.fromJson(body, Task.class);
                        if (id < 0) { //recordTasks
                            try {
                                manager.recordTasks(task);
                                httpExchange.sendResponseHeaders(201, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        } else {  //updateTask
                            try {
                                //поиск статуса ведем только после полей имени и описания, чтобы исключить ошибку
                                int beginIndexOfFindStatus = body.indexOf("status");
                                String findStatus = body.substring(beginIndexOfFindStatus);
                                if (body.contains("NEW")) {
                                    manager.updateTask(id, task, TaskStatuses.NEW);
                                } else if (body.contains("IN_PROGRESS")) {
                                    manager.updateTask(id, task, TaskStatuses.IN_PROGRESS);
                                } else if (body.contains("DONE")) {
                                    manager.updateTask(id, task, TaskStatuses.DONE);
                                }
                                httpExchange.sendResponseHeaders(201, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        }
                    } else if (splitString[2].equals("subtask")) {
                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        if (id < 0) {  //recordSubtasks
                            try {
                                manager.recordSubtasks(subtask, subtask.getEpicId());
                                httpExchange.sendResponseHeaders(201, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        } else {  //updateSubtask
                            try {
                                //поиск статуса ведем только после полей имени и описания, чтобы исключить ошибку
                                int beginIndexOfFindStatus = body.indexOf("status");
                                String findStatus = body.substring(beginIndexOfFindStatus);
                                if (body.contains("NEW")) {
                                    manager.updateTask(id, subtask, TaskStatuses.NEW);
                                } else if (body.contains("IN_PROGRESS")) {
                                    manager.updateTask(id, subtask, TaskStatuses.IN_PROGRESS);
                                } else if (body.contains("DONE")) {
                                    manager.updateTask(id, subtask, TaskStatuses.DONE);
                                }
                                httpExchange.sendResponseHeaders(201, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        }
                    } else if (splitString[2].equals("epic")) {
                        Epic epic = gson.fromJson(body, Epic.class);
                        if (id < 0) {  //recordEpics
                            try {
                                manager.recordEpics(epic);
                                httpExchange.sendResponseHeaders(201, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        } else {  //updateEpic
                            try {
                                manager.updateEpic(id, epic);
                                httpExchange.sendResponseHeaders(201, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        }
                    }
                    break;

                case "DELETE":
                    if (splitString[2].equals("task")) {
                        if (id < 0) {  //removeTasks
                            try {
                                manager.removeTasks();
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        } else {  //removeTask
                            try {
                                manager.removeTask(id);
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        }
                    }
                    if (splitString[2].equals("subtask")) {
                        if (id < 0) {  //removeSubtasks
                            try {
                                manager.removeSubtasks();
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        } else {  //removeSubtask
                            try {
                                manager.removeSubtask(id);
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        }
                    } else if (splitString[2].equals("epic")) {
                        if (id < 0) {  //removeEpics
                            try {
                                manager.removeEpics();
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        } else {  //removeEpic
                            try {
                                manager.removeEpic(id);
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        }
                    }
                    break;

                case "GET":
                    if (splitString.length < 3) { //getPrioritizedTasks
                        Set<Task> taskList = manager.getPrioritizedTasks();
                        response = gson.toJson(taskList);
                        httpExchange.sendResponseHeaders(200, 0);
                    } else if (splitString[2].equals("task")){
                        if (id < 0) {  //getTaskList
                            ArrayList<Task> taskList = manager.getTaskList();
                            response = gson.toJson(taskList);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {  //getTaskById
                            try {
                                response = gson.toJson(manager.getTaskById(id));
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        }
                    } else if (splitString[2].equals("subtask")) {
                        if (splitString.length < 4) {
                            if (id < 0) {  //getSubtaskList
                                ArrayList<Subtask> subTaskList = manager.getSubtaskList();
                                response = gson.toJson(subTaskList);
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {  //getSubtaskById
                                try {
                                    response = gson.toJson(manager.getSubtaskById(id));
                                    httpExchange.sendResponseHeaders(200, 0);
                                } catch (ManagerSaveException e) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                }
                            }
                        } else if (splitString[3].equals("epic")) {  //getEpicIdSubtasks
                            ArrayList<Subtask> subTaskList = manager.getEpicIdSubtasks(id);
                            response = gson.toJson(subTaskList);
                            httpExchange.sendResponseHeaders(200, 0);
                        }
                    } else if (splitString[2].equals("epic")) {
                        if (id < 0) {  //getEpicList
                            ArrayList<Epic> epicsList = manager.getEpicList();
                            response = gson.toJson(epicsList);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {  //getEpicById
                            try {
                                response = gson.toJson(manager.getEpicById(id));
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        }
                    } else if (splitString[2].equals("history")) {
                        List<Task> taskHistory = manager.getHistory();
                        response = gson.toJson(taskHistory);
                        httpExchange.sendResponseHeaders(200, 0);
                    }
                    break;
            }

            try (OutputStream os = httpExchange.getResponseBody()) {
                os.write(response.getBytes());
            }
        }
    }
}

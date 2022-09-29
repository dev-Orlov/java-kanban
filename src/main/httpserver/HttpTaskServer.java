package main.httpserver;

import com.google.gson.Gson;
import com.sun.net.httpserver.HttpExchange;
import com.sun.net.httpserver.HttpHandler;
import com.sun.net.httpserver.HttpServer;
import main.exceptions.ManagerSaveException;
import management.taskManagement.TaskManager;
import main.tasks.Epic;
import main.tasks.Subtask;
import main.tasks.Task;
import main.utils.Managers;
import main.utils.TaskStatuses;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.InetSocketAddress;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class HttpTaskServer {
    private static final int PORT = 8080;
    private static final Charset DEFAULT_CHARSET = StandardCharsets.UTF_8;
    public static final TaskManager MANAGER = Managers.getFileManager();
    private static HttpServer httpServer;

    public void startServer() throws IOException, ManagerSaveException {
        httpServer = HttpServer.create();
        httpServer.bind(new InetSocketAddress("localhost",  PORT), 0);
        httpServer.createContext("/tasks", new TasksHandler());
        httpServer.start();

        System.out.println("HTTP-сервер запущен на " + PORT + " порту!");
    }

    public void stopServer() {
        httpServer.stop(1);
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
                    if ("task".equals(splitString[2])) {
                        Task task = gson.fromJson(body, Task.class);
                        if (id < 0) { //recordTasks
                            try {
                                MANAGER.recordTasks(task);
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
                                    MANAGER.updateTask(id, task, TaskStatuses.NEW);
                                } else if (body.contains("IN_PROGRESS")) {
                                    MANAGER.updateTask(id, task, TaskStatuses.IN_PROGRESS);
                                } else if (body.contains("DONE")) {
                                    MANAGER.updateTask(id, task, TaskStatuses.DONE);
                                }
                                httpExchange.sendResponseHeaders(201, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        }
                    } else if ("subtask".equals(splitString[2])) {
                        Subtask subtask = gson.fromJson(body, Subtask.class);
                        if (id < 0) {  //recordSubtasks
                            try {
                                MANAGER.recordSubtasks(subtask, subtask.getEpicId());
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
                                    MANAGER.updateTask(id, subtask, TaskStatuses.NEW);
                                } else if (body.contains("IN_PROGRESS")) {
                                    MANAGER.updateTask(id, subtask, TaskStatuses.IN_PROGRESS);
                                } else if (body.contains("DONE")) {
                                    MANAGER.updateTask(id, subtask, TaskStatuses.DONE);
                                }
                                httpExchange.sendResponseHeaders(201, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        }
                    } else if ("epic".equals(splitString[2])) {
                        Epic epic = gson.fromJson(body, Epic.class);
                        if (id < 0) {  //recordEpics
                            try {
                                MANAGER.recordEpics(epic);
                                httpExchange.sendResponseHeaders(201, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        } else {  //updateEpic
                            try {
                                MANAGER.updateEpic(id, epic);
                                httpExchange.sendResponseHeaders(201, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        }
                    }
                    break;

                case "DELETE":
                    if ("task".equals(splitString[2])) {
                        if (id < 0) {  //removeTasks
                            try {
                                MANAGER.removeTasks();
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        } else {  //removeTask
                            try {
                                MANAGER.removeTask(id);
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        }
                    }
                    if ("subtask".equals(splitString[2])) {
                        if (id < 0) {  //removeSubtasks
                            try {
                                MANAGER.removeSubtasks();
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        } else {  //removeSubtask
                            try {
                                MANAGER.removeSubtask(id);
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        }
                    } else if ("epic".equals(splitString[2])) {
                        if (id < 0) {  //removeEpics
                            try {
                                MANAGER.removeEpics();
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(400, 0);
                            }
                        } else {  //removeEpic
                            try {
                                MANAGER.removeEpic(id);
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        }
                    }
                    break;

                case "GET":
                    if (splitString.length < 3) { //getPrioritizedTasks
                        Set<Task> taskList = MANAGER.getPrioritizedTasks();
                        response = gson.toJson(taskList);
                        httpExchange.sendResponseHeaders(200, 0);
                    } else if ("task".equals(splitString[2])){
                        if (id < 0) {  //getTaskList
                            ArrayList<Task> taskList = MANAGER.getTaskList();
                            response = gson.toJson(taskList);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {  //getTaskById
                            try {
                                response = gson.toJson(MANAGER.getTaskById(id));
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        }
                    } else if ("subtask".equals(splitString[2])) {
                        if (splitString.length < 4) {
                            if (id < 0) {  //getSubtaskList
                                ArrayList<Subtask> subTaskList = MANAGER.getSubtaskList();
                                response = gson.toJson(subTaskList);
                                httpExchange.sendResponseHeaders(200, 0);
                            } else {  //getSubtaskById
                                try {
                                    response = gson.toJson(MANAGER.getSubtaskById(id));
                                    httpExchange.sendResponseHeaders(200, 0);
                                } catch (ManagerSaveException e) {
                                    httpExchange.sendResponseHeaders(404, 0);
                                }
                            }
                        } else if ("epic".equals(splitString[3])) {  //getEpicIdSubtasks
                            ArrayList<Subtask> subTaskList = MANAGER.getEpicIdSubtasks(id);
                            response = gson.toJson(subTaskList);
                            httpExchange.sendResponseHeaders(200, 0);
                        }
                    } else if ("epic".equals(splitString[2])) {
                        if (id < 0) {  //getEpicList
                            ArrayList<Epic> epicsList = MANAGER.getEpicList();
                            response = gson.toJson(epicsList);
                            httpExchange.sendResponseHeaders(200, 0);
                        } else {  //getEpicById
                            try {
                                response = gson.toJson(MANAGER.getEpicById(id));
                                httpExchange.sendResponseHeaders(200, 0);
                            } catch (ManagerSaveException e) {
                                httpExchange.sendResponseHeaders(404, 0);
                            }
                        }
                    } else if ("history".equals(splitString[2])) {
                        List<Task> taskHistory = MANAGER.getHistory();
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

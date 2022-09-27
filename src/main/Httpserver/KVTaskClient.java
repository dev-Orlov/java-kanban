package main.Httpserver;

import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class KVTaskClient {

    private final String url;
    private final String apiToken;

    public KVTaskClient(String url) {
        this.url = url;
        this.apiToken = register(url);
    }

    private String register(String url) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest build = HttpRequest.newBuilder()
                    .uri(URI.create(url + "register"))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(build, HttpResponse.BodyHandlers.ofString());
            return send.body();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public void put(String key, String value) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest build = HttpRequest.newBuilder()
                    .uri(URI.create(url + "save/" + key + "?API_TOKEN=" + apiToken))
                    .POST(HttpRequest.BodyPublishers.ofString(value))
                    .build();
            HttpResponse<String> send = httpClient.send(build, HttpResponse.BodyHandlers.ofString());
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }

    public String load(String key) {
        try {
            HttpClient httpClient = HttpClient.newHttpClient();
            HttpRequest build = HttpRequest.newBuilder()
                    .uri(URI.create(url + "load/" + key + "?API_TOKEN=" + apiToken))
                    .GET()
                    .build();
            HttpResponse<String> send = httpClient.send(build, HttpResponse.BodyHandlers.ofString());
            return send.body();
        } catch (Exception e) {
            throw new RuntimeException();
        }
    }
}

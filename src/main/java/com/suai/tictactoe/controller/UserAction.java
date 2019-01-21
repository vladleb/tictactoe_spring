package com.suai.tictactoe.controller;

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.suai.tictactoe_spring.server.model.Coordinates;
import com.suai.tictactoe_spring.server.model.Room;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.stomp.StompSession;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Scanner;
import java.util.concurrent.ExecutionException;

@Log4j2
public class UserAction {

    private String sendGet(String url, Map<String, String> property) throws IOException {
        url += "?";
        StringBuilder urlBuilder = new StringBuilder(url);
        for (String key : property.keySet()) {
            urlBuilder.append(key).append("=").append(property.get(key)).append("&");
        }
        url = urlBuilder.toString();
        url = url.substring(0, url.length() - 1);
        URL obj = new URL(url);
        HttpURLConnection con = (HttpURLConnection) obj.openConnection();
        con.setRequestMethod("GET");

        return new BufferedReader(new InputStreamReader(con.getInputStream())).readLine();
    }

    public String createUsername() throws IOException {
        Map<String, String> property = new HashMap<>();
        Scanner scanner = new Scanner(System.in);

        String url, response = "", username = "";
        url = "http://localhost:8080/addUser";
        while (!response.equals("OK")) {
            log.info("Username: ");
            username = scanner.nextLine();
            property.put("name", username);
            response = sendGet(url, property);
            if (!response.equals("OK")) {
                log.info("Имя пользователя уже занято");
            }
        }

        return username;
    }

    public void createRoom(String name) throws IOException {
        Map<String, String> property = new HashMap<>();
        property.put("name", name);
        String url = "http://localhost:8080/addRoom";
        sendGet(url, property);
    }

    public List<Room> getRoomList() throws IOException {
        String url = "http://localhost:8080/rooms";
        String response = sendGet(url, new HashMap<>());
        return new ObjectMapper().readValue(response, new TypeReference<List<Room>>() {
        });
    }

    public void connectToRoom(String owner) throws IOException {
        Map<String, String> property = new HashMap<>();
        property.put("owner", owner);
        String url = "http://localhost:8080/connectToRoom";
        sendGet(url, property);
    }

    public void deleteUser(String username) throws IOException {
        Map<String, String> property = new HashMap<>();
        property.put("name", username);
        String url = "http://localhost:8080/deleteUser";
        sendGet(url, property);
    }

    public void deleteUserFromRoom(String username) throws IOException {
        Map<String, String> property = new HashMap<>();
        property.put("name", username);
        String url = "http://localhost:8080/deleteUserFromRoom";
        sendGet(url, property);
    }
}

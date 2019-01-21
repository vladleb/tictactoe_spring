package com.suai.tictactoe_spring.server.controller;

import com.suai.tictactoe_spring.server.model.*;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;
import java.util.stream.Collectors;


@Controller
public class RoomsController {

    @GetMapping("/rooms")
    @ResponseBody
    public List<Room> roomList() {
        return  ModelRoom.getInstance().getModelRoomList();
    }

    @GetMapping("/addUser")
    @ResponseBody
    public String addUser(String name) {
        ModelUser instance = ModelUser.getInstance();
        if (!instance.list().stream().filter(it -> it.equals(name)).collect(Collectors.toList()).isEmpty()) {
            return "Error";
        } else {
            instance.add(new User(name));
            return "OK";
        }
    }

    @GetMapping("/deleteUser")
    @ResponseBody
    public String deleteUser(String name) {
        ModelUser.getInstance().deleteUser(name);
        return "OK";
    }

    @GetMapping("/addRoom")
    @ResponseBody
    public String addRoom(String name) {
        ModelRoom.getInstance().add(new Room(name));
        return "OK";
    }

    @GetMapping("/deleteUserFromRoom")
    @ResponseBody
    public String deleteUserFromRoom(String name) {
        List<Room> modelRoomList = ModelRoom.getInstance().getModelRoomList();
        for (Room room : modelRoomList) {
            if(room.getGuest() != null && room.getGuest().equals(name)) {
                room.setNumberOfUsers(1);
                room.setGuest(null);
                return "OK";
            }
        }
        ModelRoom.getInstance().deleteRoom(name);
        return "OK";
    }

    @GetMapping("/connectToRoom")
    @ResponseBody
    public void connectToRoom(String owner, String guest) {
        ModelRoom.setNumberOfUsersAndGuest(owner, 2, guest);
    }

    @MessageMapping("/rooms/{room}")
    @SendTo("/topic/rooms/{room}")
    public Coordinates coordinates(Coordinates coordinate) {
        return coordinate;
    }
}

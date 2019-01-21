package com.suai.tictactoe_spring.server.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModelRoom {
    @Getter
    private static final ModelRoom instance = new ModelRoom();
    @Getter
    private final List<Room> modelRoomList;

    private ModelRoom() {
        modelRoomList = new ArrayList<>();
    }

    public void add(Room room) {
        modelRoomList.add(room);
    }

    public List<String> list() {
        return modelRoomList.stream()
                .map(Room::getOwner)
                .collect(Collectors.toList());
    }

    public void deleteRoom(String owner) {
        for (Room room : modelRoomList) {
            if (room.getOwner().equals(owner)) {
                modelRoomList.remove(room);
                break;
            }
        }
    }

    public static void setNumberOfUsersAndGuest(String owner, int number, String guest) {
        for (Room roomFromList : instance.modelRoomList) {
            if (roomFromList.getOwner().equals(owner)) {
                roomFromList.setNumberOfUsers(number);
                roomFromList.setGuest(guest);
                break;
            }
        }
    }
}

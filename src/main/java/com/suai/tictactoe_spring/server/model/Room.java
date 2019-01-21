package com.suai.tictactoe_spring.server.model;

import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
public class Room {
    private String owner;
    private String guest;
    private int numberOfUsers;

    public Room(String name) {
        this.owner = name;
        numberOfUsers = 1;
    }
}

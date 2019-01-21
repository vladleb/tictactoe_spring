package com.suai.tictactoe_spring.server.model;

import lombok.Data;

@Data
public class Coordinates {
    private final int x;
    private final int y;

    @Override
    public String toString () {
        StringBuilder sb = new StringBuilder();
        sb.append("{ \"x\" : \"").append(x).append("\", \"y\" : \"").append(y).append("\" }");
        return sb.toString();
    }
}

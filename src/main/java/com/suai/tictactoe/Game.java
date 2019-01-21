package com.suai.tictactoe;

import com.suai.tictactoe.controller.UserAction;
import com.suai.tictactoe.controller.WebSocketAction;
import com.suai.tictactoe.model.Board;
import com.suai.tictactoe.view.ConsoleInterface;
import com.suai.tictactoe_spring.server.model.Coordinates;
import lombok.extern.log4j.Log4j2;
import org.springframework.messaging.simp.stomp.StompSession;

import java.io.IOException;
import java.util.concurrent.ExecutionException;

@Log4j2
public class Game {

    private static void addShutdownHook(String name, UserAction userAction) {
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                userAction.deleteUserFromRoom(name);
                userAction.deleteUser(name);
            } catch (IOException e) {
                log.warn(e.getMessage());
            }

        }));
    }

    private static void waitCoord(ConsoleInterface interfaceOfBoard, Board board,
                                  WebSocketAction socket, Coordinates coordinates, char mark) throws InterruptedException {
        interfaceOfBoard.printBoard(board);
        while (socket.getCoordinates() == null || socket.getCoordinates().equals(coordinates)) {
            Thread.sleep(1000);
        }
        coordinates = socket.getCoordinates();
        socket.setCoordinates(null);
        board.setMark(coordinates.getX(), coordinates.getY(), mark == 'X' ? 'O' : 'X');
    }

    private static void transferOfCoordinates(ConsoleInterface interfaceOfBoard, WebSocketAction socket,
                                              StompSession stompSession, String username, String room, Board board,
                                              Coordinates coordinates) throws InterruptedException {
        while (true) {
            interfaceOfBoard.printBoard(board);
            char mark = room.equals(username) ? 'X' : 'O';
            if (mark == 'X') {
                coordinates = interfaceOfBoard.getCoordinates();
                board.setMark(coordinates.getX(), coordinates.getY(), mark);
                socket.send(stompSession, coordinates, room);
                waitCoord(interfaceOfBoard, board, socket, coordinates, mark);
            } else {
                waitCoord(interfaceOfBoard, board, socket, coordinates, mark);
                coordinates = interfaceOfBoard.getCoordinates();
                board.setMark(coordinates.getX(), coordinates.getY(), mark);
                socket.send(stompSession, coordinates, room);
            }

            if (interfaceOfBoard.checkWin(board, mark)) {
                break;
            }
        }
    }

    private static void runGame(ConsoleInterface interfaceOfBoard, UserAction userAction,
                                WebSocketAction socket, StompSession stompSession, String username)
            throws IOException, InterruptedException {
        while (true) {
            String room = interfaceOfBoard.changeOrCreateRoom(username);
            Board board = new Board();
            socket.subscribe(stompSession, room);
            Coordinates coordinates = new Coordinates(11, 11);
            transferOfCoordinates(interfaceOfBoard, socket, stompSession, username, room, board, coordinates);

            if (!interfaceOfBoard.checkReplay()) {
                break;
            }
            userAction.deleteUserFromRoom(room);
        }
    }

    public static void main(String[] args) throws IOException, ExecutionException, InterruptedException {
        ConsoleInterface interfaceOfBoard = new ConsoleInterface();
        UserAction userAction = new UserAction();
        WebSocketAction socket = new WebSocketAction();
        StompSession stompSession = socket.connect().get();
        String username = userAction.createUsername();
        addShutdownHook(username, userAction);

        runGame(interfaceOfBoard, userAction, socket, stompSession, username);
    }
}

package com.suai.tictactoe.view;

import com.suai.tictactoe.controller.UserAction;
import com.suai.tictactoe.model.Board;
import com.suai.tictactoe_spring.server.model.Coordinates;
import com.suai.tictactoe_spring.server.model.Room;
import lombok.extern.log4j.Log4j2;

import java.io.IOException;
import java.util.List;
import java.util.Scanner;

@Log4j2
public class ConsoleInterface {

    public String changeOrCreateRoom(String username) throws IOException {
        Scanner scanner = new Scanner(System.in);
        UserAction userAction = new UserAction();
        List<Room> rooms = userAction.getRoomList();

        if (rooms.isEmpty()) {
            log.info("Доступных комнат нет, хотите создать свою? [Y/n]: ");
            String answer = scanner.nextLine();
            if (answer.equals("y") || answer.equals("") || answer.equals("Y")) {
                userAction.createRoom(username);

                log.info("create");
                return username;
            } else {
                System.exit(0);
            }
        } else {
            log.info("0. Создать свою комнату.");
            for (int i = 0; i < rooms.size(); i++) {
                log.info((i + 1) + ". " + rooms.get(i).getOwner() + " : " + rooms.get(i).getNumberOfUsers());
            }

            String choice;
            while (true) {
                log.info("Выбери комнату в которой есть место: ");
                choice = scanner.nextLine().replaceAll("[^\\d]", "");
                int choiceInt = Integer.parseInt(choice) - 1;
                if (choiceInt == -1) {
                    userAction.createRoom(username);

                    log.info("create");
                    return username;
                } else if (rooms.get(choiceInt).getNumberOfUsers() == 1) {
                    String owner = rooms.get(choiceInt).getOwner();
                    userAction.connectToRoom(owner);
                    return owner;
                }
            }

        }

        return username;
    }

    public void printBoard (Board board) {
        log.info(board.toString());
    }

    public Coordinates getCoordinates () {
        log.info("Введите координаты, где хотите нарисовать отметку (пример: 2 4)");
        Scanner scanner = new Scanner(System.in);
        String[] coordinates = scanner.nextLine().split(" ");
        return new Coordinates(Integer.parseInt(coordinates[0]) - 1, Integer.parseInt(coordinates[1]) - 1);
    }

    public boolean checkReplay() {
        log.info("Хотите сыграть снова? [Y/n]");
        Scanner scanner = new Scanner(System.in);
        String choice = scanner.nextLine();
        return choice.equals("") || choice.equals("Y") || choice.equals("y");
    }

    public boolean checkWin(Board board, char mark) {
        int checkWin = board.checkWin();
        if (checkWin != -1) {
            if (checkWin == 0) {
                log.info("Ничья!");
            } else if (checkWin == 1) {
                log.info(mark == 'X' ? "Вы победили!" : "Вы проиграли!");
            } else {
                log.info(mark == 'O' ? "Вы победили!" : "Вы проиграли!");
            }
            return true;
        }

        return false;
    }
}

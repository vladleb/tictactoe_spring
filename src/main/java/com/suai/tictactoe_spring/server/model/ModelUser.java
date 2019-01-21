package com.suai.tictactoe_spring.server.model;

import lombok.Getter;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ModelUser {
    @Getter
    private static final ModelUser instance = new ModelUser();
    @Getter
    private final List<User> modelUserList;

    private ModelUser() {
        modelUserList = new ArrayList<>();
    }

    public void add(User user) {
        modelUserList.add(user);
    }

    public List<String> list() {
        return modelUserList.stream()
                .map(User::getUsername)
                .collect(Collectors.toList());
    }

    public void deleteUser(String owner) {
        for (User user : modelUserList) {
            if (user.getUsername().equals(owner)) {
                modelUserList.remove(user);
                break;
            }
        }
    }
}

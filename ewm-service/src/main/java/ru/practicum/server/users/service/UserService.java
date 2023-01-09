package ru.practicum.server.users.service;

import ru.practicum.server.users.model.User;

import java.util.List;

public interface UserService {

    List<User> getUsers(List<Long> ids, Integer from, Integer size);

    User addUser(User newUser);

    void deleteUser(Long userId);

    User getUserById(Long userId);
}

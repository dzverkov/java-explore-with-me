package ru.practicum.server.users.exception;

import ru.practicum.server.exception.NotFoundException;

public class UserNotFoundException extends NotFoundException {
    public UserNotFoundException(Long id) {
        super("Пользователь с Id: " + id + " не найден.");
    }
}

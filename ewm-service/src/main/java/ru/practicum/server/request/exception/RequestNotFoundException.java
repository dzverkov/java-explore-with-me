package ru.practicum.server.request.exception;

import ru.practicum.server.exception.NotFoundException;

public class RequestNotFoundException extends NotFoundException {
    public RequestNotFoundException(Long id) {
        super("Запрос на участие с Id: " + id + " не найдено.");
    }
}

package ru.practicum.server.locations.exception;

import ru.practicum.server.exception.NotFoundException;

public class EventLocationTypeNotFound extends NotFoundException {
    public EventLocationTypeNotFound(Long id) {
        super("Тип локации с Id: " + id + " не найден.");
    }
}

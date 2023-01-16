package ru.practicum.server.locations.exception;

import ru.practicum.server.exception.NotFoundException;

public class EventLocationNotFound extends NotFoundException {
    public EventLocationNotFound(Long id) {
        super("Локация с Id: " + id + " не найдена.");
    }
}

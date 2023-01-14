package ru.practicum.server.events.exception;

import ru.practicum.server.exception.NotFoundException;

public class EventNotFoundException extends NotFoundException {
    public EventNotFoundException(Long id) {
        super("Событие с Id: " + id + " не найдено.");
    }

    public EventNotFoundException(Long eventId, Long userId) {
        super("У пользователя userId: " + userId + " нет события eventId: " + eventId);
    }
}

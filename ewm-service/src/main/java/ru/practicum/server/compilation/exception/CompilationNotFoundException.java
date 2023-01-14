package ru.practicum.server.compilation.exception;

import ru.practicum.server.exception.NotFoundException;

public class CompilationNotFoundException extends NotFoundException {
    public CompilationNotFoundException(Long id) {
        super("Подборка событий с Id: " + id + " не найдено.");
    }
}

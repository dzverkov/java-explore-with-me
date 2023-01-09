package ru.practicum.server.categories.exception;

import ru.practicum.server.exception.NotFoundException;

public class CategoryNotFoundException extends NotFoundException {
    public CategoryNotFoundException(Long id) {
        super("Категория с Id: " + id + " не найдена.");
    }
}

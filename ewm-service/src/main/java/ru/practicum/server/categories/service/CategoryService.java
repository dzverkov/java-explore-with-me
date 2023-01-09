package ru.practicum.server.categories.service;

import ru.practicum.server.categories.model.Category;

import java.util.List;

public interface CategoryService {
    Category updateCategory(Category updCategory);

    Category addCategory(Category newCategory);

    void deleteCategory(Long catId);

    List<Category> getAllCategories(Integer from, Integer size);

    Category getCategoryById(Long catId);
}

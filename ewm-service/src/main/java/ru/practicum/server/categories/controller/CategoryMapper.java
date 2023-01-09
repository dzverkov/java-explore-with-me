package ru.practicum.server.categories.controller;

import org.mapstruct.Mapper;
import ru.practicum.server.categories.dto.CategoryDto;
import ru.practicum.server.categories.dto.NewCategoryDto;
import ru.practicum.server.categories.model.Category;

@Mapper
public interface CategoryMapper {

    Category toCategory(CategoryDto categoryDto);

    Category toCategory(NewCategoryDto categoryDto);

    CategoryDto toCategoryDto(Category category);

}

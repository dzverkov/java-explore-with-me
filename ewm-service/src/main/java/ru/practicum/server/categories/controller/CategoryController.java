package ru.practicum.server.categories.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.categories.dto.CategoryDto;
import ru.practicum.server.categories.dto.NewCategoryDto;
import ru.practicum.server.categories.model.Category;
import ru.practicum.server.categories.repository.CategoryRepository;
import ru.practicum.server.categories.service.CategoryService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Validated
@RequestMapping
@RequiredArgsConstructor
public class CategoryController {
    private static final String ADMIN_CATEGORY_PATH = "/admin/categories";
    private static final String PUBLIC_CATEGORY_PATH = "/categories";
    private final CategoryRepository categoryRepository;
    private final CategoryService categoryService;
    private final CategoryMapper mapper = Mappers.getMapper(CategoryMapper.class);


    /////////////////////////////////////
    // Public: Категории
    // Публичный API для работы с категориями
    /////////////////////////////////////

    @GetMapping(PUBLIC_CATEGORY_PATH)
    ResponseEntity<Object> getAllCategories(
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size
    ) {
        log.info("Получен запрос GET на получение всех категорий.");
        List<Category> categoryList = categoryService.getAllCategories(from, size);
        List<CategoryDto> categoryDtoList = categoryList.stream().map(mapper::toCategoryDto).collect(Collectors.toList());
        return ResponseEntity.ok(categoryDtoList);
    }

    @GetMapping(PUBLIC_CATEGORY_PATH + "/{catId}")
    ResponseEntity<Object> getCategoryById(
            @PathVariable @Positive Long catId
    ) {
        log.info("Получен запрос GET на получение категории с Id: {}.", catId);
        return ResponseEntity.ok(mapper.toCategoryDto(categoryService.getCategoryById(catId)));
    }

    /////////////////////////////////////
    // Admin: Категории
    // API для работы с категориями
    /////////////////////////////////////

    @PatchMapping(ADMIN_CATEGORY_PATH)
    ResponseEntity<Object> updateCategory(
            @RequestBody @Valid CategoryDto categoryDto) {

        log.info("Получен запрос PATCH на изменение категории, параметр: {}.", categoryDto);
        Category category = mapper.toCategory(categoryDto);
        return ResponseEntity.ok(mapper.toCategoryDto(categoryService.updateCategory(category)));
    }

    @PostMapping(ADMIN_CATEGORY_PATH)
    ResponseEntity<Object> addCategory(
            @RequestBody @Valid NewCategoryDto newCategoryDto) {

        log.info("Получен запрос POST на добавление категории, параметр: {}.", newCategoryDto);
        Category category = mapper.toCategory(newCategoryDto);
        return ResponseEntity.ok(mapper.toCategoryDto(categoryService.addCategory(category)));
    }

    @DeleteMapping(ADMIN_CATEGORY_PATH + "/{catId}")
    ResponseEntity<Object> deleteCategory(
            @PathVariable @Positive Long catId) {
        log.info("Получен запрос DELETE на удаление категории с Id:{}.", catId);
        categoryService.deleteCategory(catId);
        return ResponseEntity.ok().build();
    }
}

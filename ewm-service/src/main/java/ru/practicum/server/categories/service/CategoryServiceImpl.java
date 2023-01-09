package ru.practicum.server.categories.service;

import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import ru.practicum.server.categories.model.Category;
import ru.practicum.server.categories.repository.CategoryRepository;
import ru.practicum.server.exception.ConflictException;
import ru.practicum.server.exception.NotFoundException;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class CategoryServiceImpl implements CategoryService {

    private final CategoryRepository categoryRepository;


    @Override
    public List<Category> getAllCategories(Integer from, Integer size) {
        Integer page = from / size;
        Pageable pageable = PageRequest.of(page, size);
        return categoryRepository.findAll(pageable).getContent();
    }

    @Override
    public Category getCategoryById(Long catId) {
        return categoryRepository.findById(catId)
                .orElseThrow(() -> new NotFoundException("Категория с id: " + catId + " не найдена."));
    }

    @Override
    public Category updateCategory(Category updCategory) {
        categoryRepository.findById(updCategory.getId())
                .orElseThrow(() -> new NotFoundException("Категория с id: " + updCategory.getId() + " не найдена."));

        Optional<Category> category = categoryRepository.findByName(updCategory.getName());

        if (category.isPresent()) {
            throw new ConflictException("Такая категория уже существует");
        }

        return categoryRepository.save(updCategory);
    }

    @Override
    public Category addCategory(Category newCategory) {

        Optional<Category> category = categoryRepository.findByName(newCategory.getName());

        if (category.isPresent()) {
            throw new ConflictException("Такая категория уже существует");
        }

        return categoryRepository.save(newCategory);
    }

    @Override
    public void deleteCategory(Long catId) {
        categoryRepository.deleteById(catId);
    }

}

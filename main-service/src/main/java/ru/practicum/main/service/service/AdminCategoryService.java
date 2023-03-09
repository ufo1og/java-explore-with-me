package ru.practicum.main.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.main.service.dto.CategoryDto;
import ru.practicum.main.service.dto.NewCategoryDto;
import ru.practicum.main.service.model.Category;
import ru.practicum.main.service.repository.CategoryRepository;
import ru.practicum.main.service.utils.CategoryConverter;

import javax.persistence.EntityNotFoundException;

@Service
@RequiredArgsConstructor
@Slf4j
public class AdminCategoryService {
    private final CategoryRepository categoryRepository;

    public ResponseEntity<Object> createNewCategory(NewCategoryDto categoryRequest) {
        Category createdCategory = CategoryConverter.toCategory(categoryRequest);
        createdCategory = categoryRepository.save(createdCategory);
        log.info("Created new Category: {}", createdCategory);
        CategoryDto categoryDto = CategoryConverter.toCategoryDto(createdCategory);
        return new ResponseEntity<>(categoryDto, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> updateCategory(long categoryId, NewCategoryDto categoryRequest) {
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Category with id=%s not found", categoryId)));
        category.setName(categoryRequest.getName());
        Category updatedCategory = categoryRepository.save(category);
        CategoryDto categoryDto = CategoryConverter.toCategoryDto(updatedCategory);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> deleteCategory(long categoryId) {
        if (!categoryRepository.existsById(categoryId)) {
            throw new EntityNotFoundException(String.format("Category with id=%s not found", categoryId));
        }
        categoryRepository.deleteById(categoryId);
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}

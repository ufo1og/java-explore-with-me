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
}

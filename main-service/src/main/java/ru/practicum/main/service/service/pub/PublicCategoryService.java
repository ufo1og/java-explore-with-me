package ru.practicum.main.service.service.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.main.service.dto.CategoryDto;
import ru.practicum.main.service.model.Category;
import ru.practicum.main.service.repository.CategoryRepository;
import ru.practicum.main.service.utils.CategoryConverter;

import javax.persistence.EntityNotFoundException;
import java.util.List;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicCategoryService {
    private final CategoryRepository categoryRepository;

    public ResponseEntity<Object> getAllCategories(Pageable pageable) {
        List<Category> categories = categoryRepository.findAll(pageable).toList();
        List<CategoryDto> categoryDtos = categories.stream()
                .map(CategoryConverter::toCategoryDto)
                .collect(Collectors.toList());
        return new ResponseEntity<>(categoryDtos, HttpStatus.OK);
    }

    public ResponseEntity<Object> getCategory(long catId) {
        Category category = categoryRepository.findById(catId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Category with id = %s not found", catId)));
        CategoryDto categoryDto = CategoryConverter.toCategoryDto(category);
        return new ResponseEntity<>(categoryDto, HttpStatus.OK);
    }
}
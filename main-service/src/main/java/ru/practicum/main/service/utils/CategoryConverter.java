package ru.practicum.main.service.utils;

import ru.practicum.main.service.dto.CategoryDto;
import ru.practicum.main.service.dto.NewCategoryDto;
import ru.practicum.main.service.model.Category;

public class CategoryConverter {
    public static Category toCategory(NewCategoryDto categoryDto) {
        return new Category(null, categoryDto.getName());
    }

    public static CategoryDto toCategoryDto(Category category) {
        return new CategoryDto(category.getId(), category.getName());
    }
}

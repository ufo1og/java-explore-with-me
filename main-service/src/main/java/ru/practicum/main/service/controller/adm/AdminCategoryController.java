package ru.practicum.main.service.controller.adm;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.NewCategoryDto;
import ru.practicum.main.service.service.adm.AdminCategoryService;

import javax.validation.Valid;

@RestController
@RequestMapping("/admin/categories")
@RequiredArgsConstructor
public class AdminCategoryController {
    private final AdminCategoryService adminCategoryService;

    @PostMapping
    public ResponseEntity<Object> createNewCategory(@Valid @RequestBody NewCategoryDto categoryRequest) {
        return adminCategoryService.createNewCategory(categoryRequest);
    }

    @PatchMapping("/{categoryId}")
    public ResponseEntity<Object> updateCategory(@PathVariable long categoryId,
                                                 @Valid @RequestBody NewCategoryDto categoryRequest) {
        return adminCategoryService.updateCategory(categoryId, categoryRequest);
    }

    @DeleteMapping("/{categoryId}")
    public ResponseEntity<Object> deleteCategory(@PathVariable long categoryId) {
        return adminCategoryService.deleteCategory(categoryId);
    }
}

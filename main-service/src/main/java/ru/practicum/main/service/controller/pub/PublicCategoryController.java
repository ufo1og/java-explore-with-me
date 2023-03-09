package ru.practicum.main.service.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.service.pub.PublicCategoryService;

import static ru.practicum.main.service.utils.PageRequestGetter.getPageRequest;

@RestController
@RequestMapping("/categories")
@RequiredArgsConstructor
public class PublicCategoryController {
    private final PublicCategoryService publicCategoryService;

    @GetMapping
    public ResponseEntity<Object> getAllCategories(@RequestParam(required = false, defaultValue = "0") int from,
                                                   @RequestParam(required = false, defaultValue = "10") int size) {
        return publicCategoryService.getAllCategories(getPageRequest(from, size));
    }

    @GetMapping("/{catId}")
    public ResponseEntity<Object> getCategory(@PathVariable long catId) {
        return publicCategoryService.getCategory(catId);
    }
}

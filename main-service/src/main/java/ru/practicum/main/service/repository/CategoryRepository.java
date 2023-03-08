package ru.practicum.main.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.service.model.Category;

public interface CategoryRepository extends JpaRepository<Category, Long> {
}

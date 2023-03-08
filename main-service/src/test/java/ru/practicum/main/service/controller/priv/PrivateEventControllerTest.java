package ru.practicum.main.service.controller.priv;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.main.service.dto.NewCategoryDto;
import ru.practicum.main.service.repository.CategoryRepository;
import ru.practicum.main.service.utils.CategoryConverter;

@DataJpaTest
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@AutoConfigureTestDatabase
public class PrivateEventControllerTest {
    private final CategoryRepository categoryRepository;

    @Test
    public void test() {
        NewCategoryDto newCategoryDto = new NewCategoryDto("Conflict");
        categoryRepository.save(CategoryConverter.toCategory(newCategoryDto));
        categoryRepository.save(CategoryConverter.toCategory(newCategoryDto));
    }
}

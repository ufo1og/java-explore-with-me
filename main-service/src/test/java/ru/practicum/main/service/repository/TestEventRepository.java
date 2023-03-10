package ru.practicum.main.service.repository;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.jdbc.Sql;
import ru.practicum.main.service.model.Event;

import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Sql("classpath:EventRepositoryTestData.sql")
public class TestEventRepository {
    private final EventRepository eventRepository;

    @Test
    public void test() {
        List<Event> events = eventRepository.getUserEvents(1L, PageRequest.of(0, 10));
        System.out.println("done");
    }
}

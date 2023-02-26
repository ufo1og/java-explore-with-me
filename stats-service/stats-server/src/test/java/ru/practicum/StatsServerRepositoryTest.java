package ru.practicum;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import ru.practicum.stats.server.repository.StatsServerRepository;
import ru.practicum.stats.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

@DataJpaTest
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class StatsServerRepositoryTest {
    private final StatsServerRepository repository;

    @Test
    public void test() {
        LocalDateTime start = LocalDateTime.of(2023, 1, 1, 0, 0);
        LocalDateTime end = LocalDateTime.of(2023, 12, 1, 0, 0);
        String[] uris = new String[]{"/events/1", "/events/2"};
        List<ViewStats> hits = repository.countHits(start, end, uris);
        System.out.println("all hits");
        System.out.println(hits);
        System.out.println("====");
        hits = repository.countUniqueHits(start, end, uris);
        System.out.println("unique hits");
        System.out.println(hits);
    }
}

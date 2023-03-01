package ru.practicum.stats.server;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.CoreMatchers.*;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.DisplayName;
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
    @DisplayName("Test getting all hits for endpoints '/events/{1, 2}'")
    public void test1() {
        List<ViewStats> hits = repository.countHits(LocalDateTime.now().minusDays(10), LocalDateTime.now().plusDays(1),
                List.of("/events/1", "/events/2"));

        assertThat(hits.size(), is(2));
        assertThat(hits.get(0).getHits(), is(5L));
        assertThat(hits.get(1).getHits(), is(4L));
    }

    @Test
    @DisplayName("Test getting unique hits for endpoints '/events/{1, 2}'")
    public void test2() {
        List<ViewStats> hits = repository.countUniqueHits(LocalDateTime.now().minusDays(10), LocalDateTime.now().plusDays(1),
                List.of("/events/1", "/events/2"));

        assertThat(hits.size(), is(2));
        assertThat(hits.get(0).getHits(), is(3L));
        assertThat(hits.get(1).getHits(), is(2L));
    }

    @Test
    @DisplayName("Test getting all hits for endpoint '/events/1'")
    public void test3() {
        List<ViewStats> hits = repository.countHits(LocalDateTime.now().minusDays(10), LocalDateTime.now().plusDays(1),
                List.of("/events/1"));

        assertThat(hits.size(), is(1));
        assertThat(hits.get(0).getHits(), is(5L));
    }

    @Test
    @DisplayName("Test getting all hits for endpoint '/events/1' for 1 day period")
    public void test4() {
        List<ViewStats> hits = repository.countHits(LocalDateTime.now().minusDays(5).minusHours(1),
                LocalDateTime.now().minusDays(4),
                List.of("/events/1"));

        assertThat(hits.size(), is(1));
        assertThat(hits.get(0).getHits(), is(2L));
    }
}

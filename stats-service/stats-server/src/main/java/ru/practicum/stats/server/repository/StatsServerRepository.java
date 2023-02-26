package ru.practicum.stats.server.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.stats.dto.ViewStats;
import ru.practicum.stats.server.model.EndpointHit;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsServerRepository extends JpaRepository<EndpointHit, Long> {
    @Query("SELECT new ru.practicum.stats.dto.ViewStats(e.app, e.uri, COUNT(e.ip))" +
            " FROM EndpointHit e" +
            " WHERE e.timestamp BETWEEN ?1 and ?2 and e.uri in ?3" +
            " GROUP BY e.app, e.uri" +
            " ORDER BY COUNT(e.ip) DESC")
    List<ViewStats> countHits(LocalDateTime start, LocalDateTime end, String[] uris);

    @Query("SELECT new ru.practicum.stats.dto.ViewStats(e.app, e.uri, COUNT(DISTINCT e.ip))" +
            " FROM EndpointHit e" +
            " WHERE e.timestamp BETWEEN ?1 and ?2 and e.uri in ?3" +
            " GROUP BY e.app, e.uri" +
            " ORDER BY COUNT(e.ip) DESC")
    List<ViewStats> countUniqueHits(LocalDateTime start, LocalDateTime end, String[] uris);
}

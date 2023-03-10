package ru.practicum.main.service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main.service.model.Event;

import java.util.List;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "select e from Event e " +
            "join fetch e.initiator " +
            "join fetch e.category " +
            "where e.initiator.id = ?1")
//    @EntityGraph(attributePaths = {"initiator", "category"})
    List<Event> getUserEvents(Long userId, Pageable pageable);
}

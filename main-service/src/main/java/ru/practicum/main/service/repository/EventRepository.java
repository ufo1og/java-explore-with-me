package ru.practicum.main.service.repository;

import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main.service.model.Event;
import ru.practicum.main.service.utils.EventState;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

public interface EventRepository extends JpaRepository<Event, Long> {
    @Query(value = "select e from Event e " +
            "join fetch e.initiator " +
            "join fetch e.category " +
            "where e.initiator.id = ?1")
    List<Event> getUserEvents(Long userId, Pageable pageable);

    @Query(value = "select e from Event e " +
            "join fetch e.initiator " +
            "join fetch  e.category " +
            "where e.id = ?1")
    Optional<Event> findByIdFetch(Long eventId);

    @Query(value = "select e from Event e " +
            "join fetch e.initiator " +
            "join fetch e.category " +
            "where e.id = ?1 and e.initiator.id = ?2")
    Optional<Event> findByIdAndInitiatorFetch(Long eventId, Long userId);

    @Query(value = "select e from Event e " +
            "join fetch e.initiator " +
            "join fetch e.category " +
            "where e.initiator.id in ?1 " +
            "and e.state in ?2 " +
            "and e.category.id in ?3 " +
            "and e.eventDate between ?4 and ?5")
    List<Event> findEventsFiltered(List<Long> users, List<EventState> states, List<Long> categories,
                                   LocalDateTime start, LocalDateTime end, Pageable pageable);
}

package ru.practicum.main.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.practicum.main.service.model.Event;

public interface EventRepository extends JpaRepository<Event, Long> {
}

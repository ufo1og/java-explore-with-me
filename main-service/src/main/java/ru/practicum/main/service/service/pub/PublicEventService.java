package ru.practicum.main.service.service.pub;

import lombok.RequiredArgsConstructor;
import lombok.Value;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.main.service.repository.EventRepository;

import java.time.LocalDateTime;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicEventService {
    @Value("")
    private String
    private final EventRepository eventRepository;


    public ResponseEntity<Object> getAllEvents(String text, long[] categories, boolean paid, LocalDateTime start,
                                               LocalDateTime end, boolean onlyAvailable, String sort,
                                               PageRequest pageRequest) {
        return null; //TODO
    }
}

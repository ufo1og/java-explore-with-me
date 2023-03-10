package ru.practicum.main.service.service.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.main.service.dto.EventShortDto;
import ru.practicum.main.service.dto.NewEventDto;
import ru.practicum.main.service.model.Category;
import ru.practicum.main.service.model.Event;
import ru.practicum.main.service.model.User;
import ru.practicum.main.service.repository.CategoryRepository;
import ru.practicum.main.service.repository.EventRepository;
import ru.practicum.main.service.repository.UserRepository;
import ru.practicum.main.service.utils.EventConverter;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.ViewStats;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrivateEventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;

    public ResponseEntity<Object> getAllEvents(long userId, Pageable pageable) {
        List<Event> foundEvents = eventRepository.getUserEvents(userId, pageable);
        if (foundEvents.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
        Map<Long, Long> viewStats = getEventViews(foundEvents);
        Map<Long, Integer> confirmedRequests = getEventConfirmedRequests(foundEvents);
        List<EventShortDto> eventShortDtos = EventConverter.toEventShortDto(foundEvents, viewStats, confirmedRequests);
        return new ResponseEntity<>(eventShortDtos, HttpStatus.OK);
    }

    public ResponseEntity<Object> createNewEvent(long userId, NewEventDto eventRequest) {
        User initiator = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%s not found", userId))
        );
        final long categoryId = eventRequest.getCategory();
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Category with id=%s not found", categoryId))
        );
        Event createdEvent = EventConverter.toNewEvent(initiator, category, eventRequest);
        createdEvent = eventRepository.save(createdEvent);
        log.info("Created new Event: {}", createdEvent);
        EventShortDto eventShortDto = EventConverter.toEventShortDto(createdEvent, category, initiator);
        return new ResponseEntity<>(eventShortDto, HttpStatus.CREATED); //TODO
    }

    private Map<Long, Long> getEventViews(List<Event> events) {
        Map<Long, Long> result = events.stream().collect(Collectors.toMap(Event::getId, event -> 0L));
        Optional<LocalDateTime> earliestPublishedDate = events.stream()
                .map(Event::getPublishedOn)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo);
        if (earliestPublishedDate.isPresent()) {
            List<String> uris = events.stream()
                    .map(event -> String.format("/events/%s", event.getId()))
                    .collect(Collectors.toList());
            LocalDateTime startRequestDate = earliestPublishedDate.get();
            LocalDateTime endRequestDate = LocalDateTime.now();
            List<ViewStats> viewStats = statsClient.getHits(startRequestDate, endRequestDate, uris, true);
            for (ViewStats s : viewStats) {
                String uri = s.getUri();
                Long eventId = Long.parseLong(uri.substring(uri.lastIndexOf("/") + 1));
                result.put(eventId, s.getHits());
            }
        }
        return result;
    }

    private Map<Long, Integer> getEventConfirmedRequests(List<Event> events) {
        Map<Long, Integer> result = events.stream().collect(Collectors.toMap(Event::getId, event -> 0));
        //TODO
        return result;
    }
}

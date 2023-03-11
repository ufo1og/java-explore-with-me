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
import ru.practicum.main.service.repository.ParticipationRequestRepository;
import ru.practicum.main.service.repository.UserRepository;
import ru.practicum.main.service.utils.EventConverter;
import ru.practicum.stats.client.StatsClient;

import javax.persistence.EntityNotFoundException;
import java.util.*;

import static ru.practicum.main.service.utils.EventStatsCalculator.getEventConfirmedRequests;
import static ru.practicum.main.service.utils.EventStatsCalculator.getEventViews;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrivateEventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;
    private final ParticipationRequestRepository participationRequestRepository;

    public ResponseEntity<Object> getAllEvents(long userId, Pageable pageable) {
        List<Event> foundEvents = eventRepository.getUserEvents(userId, pageable);
        if (foundEvents.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
        Map<Long, Long> viewStats = getEventViews(foundEvents, statsClient);
        Map<Long, Integer> confirmedRequests = getEventConfirmedRequests(foundEvents, participationRequestRepository);
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
        return new ResponseEntity<>(eventShortDto, HttpStatus.CREATED);
    }
}

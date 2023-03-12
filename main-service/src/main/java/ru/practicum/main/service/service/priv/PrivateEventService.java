package ru.practicum.main.service.service.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.main.service.dto.*;
import ru.practicum.main.service.exceptions.ForbiddenAccessException;
import ru.practicum.main.service.exceptions.ForbiddenEventDateException;
import ru.practicum.main.service.model.Category;
import ru.practicum.main.service.model.Event;
import ru.practicum.main.service.model.Location;
import ru.practicum.main.service.model.User;
import ru.practicum.main.service.repository.CategoryRepository;
import ru.practicum.main.service.repository.EventRepository;
import ru.practicum.main.service.repository.ParticipationRequestRepository;
import ru.practicum.main.service.repository.UserRepository;
import ru.practicum.main.service.utils.EventConverter;
import ru.practicum.main.service.utils.EventState;
import ru.practicum.stats.client.StatsClient;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;

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
        List<EventShortDto> eventShortDtos = EventConverter.toEventShortDto(foundEvents, statsClient,
                participationRequestRepository);
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

    public ResponseEntity<Object> getUserEvent(long userId, long eventId) {
        if (!userRepository.existsById(userId)) {
            throw new EntityNotFoundException(String.format("User with id=%s not found", userId));
        }
        Event event = eventRepository.findByIdAndInitiatorFetch(eventId, userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Event with id=%s not found", eventId))
        );
        EventFullDto eventFullDto = EventConverter.toEventFullDto(List.of(event), statsClient,
                participationRequestRepository).get(0);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }

    public ResponseEntity<Object> updateUserEvent(long userId, long eventId, UpdateEventUserRequest request) {
        Event updatingEvent = eventRepository.findByIdAndInitiatorFetch(eventId, userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Event with id=%s not found", eventId))
        );
        if (LocalDateTime.now().isAfter(updatingEvent.getEventDate().minusHours(2))) {
            throw new ForbiddenEventDateException("No access. Event will start in less than an 2 hours");
        }
        EventState eventState = updatingEvent.getState();
        if (eventState.equals(EventState.PUBLISH_EVENT) || eventState.equals(EventState.REJECT_EVENT)) {
            throw new ForbiddenAccessException("No access. Cant update published or rejected event");
        }
        Event updatedEvent = EventConverter.makeUpdatedEvent(updatingEvent, request, categoryRepository);
        updatedEvent = eventRepository.save(updatedEvent);
        log.info("Updated Event: {}", updatedEvent);
        EventFullDto eventFullDto = EventConverter.toEventFullDto(List.of(updatedEvent), statsClient,
                participationRequestRepository).get(0);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }
}

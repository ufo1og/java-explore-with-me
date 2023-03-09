package ru.practicum.main.service.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
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

import javax.persistence.EntityNotFoundException;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrivateEventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

    public ResponseEntity<Object> createNewEvent(long userId, NewEventDto eventRequest) {
        User initiator = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%s not found", userId))
        );
        final long categoryId = eventRequest.getCategory();
        Category category = categoryRepository.findById(categoryId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Category with id=%s not found", categoryId))
        );
        Event createdEvent = EventConverter.toEvent(initiator, category, eventRequest);
        createdEvent = eventRepository.save(createdEvent);
        log.info("Created new Event: {}", createdEvent);
        EventShortDto eventShortDto = EventConverter.toEventShortDto(createdEvent, category, initiator);
        return new ResponseEntity<>(eventShortDto, HttpStatus.CREATED);
    }
}

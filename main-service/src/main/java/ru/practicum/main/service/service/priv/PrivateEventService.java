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

import javax.persistence.EntityNotFoundException;
import java.util.Collections;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrivateEventService {
    private final EventRepository eventRepository;
    private final UserRepository userRepository;
    private final CategoryRepository categoryRepository;

//    private final StatsClient statsClient = new StatsClient("");

    public ResponseEntity<Object> getAllEvents(long userId, Pageable pageable) {
        List<Event> foundEvents = eventRepository.findAllByInitiator(userId, pageable);
        if (foundEvents.isEmpty()) {
            return new ResponseEntity<>(Collections.emptyList(), HttpStatus.OK);
        }
//        List<EventShortDto> eventShortDtos = foundEvents.stream()
//                .map(EventConverter::toEventShortDto)
//                .collect(Collectors.toList());
        return null; //TODO
    }

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
        return new ResponseEntity<>(eventShortDto, HttpStatus.CREATED); //TODO
    }
}

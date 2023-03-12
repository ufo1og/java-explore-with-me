package ru.practicum.main.service.service.adm;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.main.service.dto.EventFullDto;
import ru.practicum.main.service.dto.UpdateEventAdminRequest;
import ru.practicum.main.service.exceptions.ForbiddenAccessException;
import ru.practicum.main.service.exceptions.ForbiddenEventDateException;
import ru.practicum.main.service.model.Category;
import ru.practicum.main.service.model.Event;
import ru.practicum.main.service.model.Location;
import ru.practicum.main.service.repository.CategoryRepository;
import ru.practicum.main.service.repository.EventRepository;
import ru.practicum.main.service.repository.ParticipationRequestRepository;
import ru.practicum.main.service.utils.EventConverter;
import ru.practicum.main.service.utils.EventState;
import ru.practicum.stats.client.StatsClient;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;
import static ru.practicum.stats.dto.ConstantValues.TIMESTAMP_FORMATTER;

@Service
@Slf4j
@RequiredArgsConstructor
public class AdminEventService {
    private final EventRepository eventRepository;
    private final CategoryRepository categoryRepository;
    private final StatsClient statsClient;
    private final ParticipationRequestRepository participationRequestRepository;

    public ResponseEntity<Object> findEvents(List<Long> users, List<EventState> states, List<Long> categories,
                                             LocalDateTime start, LocalDateTime end, PageRequest pageRequest) {
        List<Event> events = eventRepository.findEventsFiltered(users, states, categories, start, end, pageRequest);
        List<EventFullDto> eventFullDtos = EventConverter.toEventFullDto(events, statsClient,
                participationRequestRepository);
        return new ResponseEntity<>(eventFullDtos, HttpStatus.OK);
    }

    public ResponseEntity<Object> updateEvent(long eventId, UpdateEventAdminRequest request) {
        Event updatingEvent = eventRepository.findByIdFetch(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Event with id =%s not found", eventId))
        );
        if (LocalDateTime.now().isAfter(updatingEvent.getEventDate().minusHours(1))) {
            throw new ForbiddenEventDateException("No access. Event will start in less than an hour");
        }
        if (updatingEvent.getState().equals(EventState.CANCEL_REVIEW)) {
            throw new ForbiddenAccessException("No access. Cant update cancelled Event");
        }
        if (updatingEvent.getPublished()) {
            throw new ForbiddenAccessException("No access. Cant update published Event");
        }
        Event updatedEvent = EventConverter.makeUpdatedEvent(updatingEvent, request, categoryRepository);
        updatedEvent = eventRepository.save(updatedEvent);
        log.info("Updated Event: {}", updatedEvent);
        EventFullDto eventFullDto = EventConverter.toEventFullDto(List.of(updatedEvent), statsClient,
                        participationRequestRepository).get(0);
        return new ResponseEntity<>(eventFullDto, HttpStatus.OK);
    }
}

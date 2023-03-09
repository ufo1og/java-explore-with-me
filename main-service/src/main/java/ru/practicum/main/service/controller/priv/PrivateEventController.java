package ru.practicum.main.service.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.NewEventDto;
import ru.practicum.main.service.exceptions.ForbiddenEventDateException;
import ru.practicum.main.service.service.priv.PrivateEventService;

import javax.validation.Valid;
import java.time.LocalDateTime;

import static ru.practicum.stats.dto.ConstantValues.TIMESTAMP_FORMATTER;
import static ru.practicum.main.service.utils.PageRequestGetter.getPageRequest;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {
    private final PrivateEventService privateEventService;

    @GetMapping
    public ResponseEntity<Object> getAllEvents(@PathVariable long userId,
                                               @RequestParam(required = false, defaultValue = "0") int from,
                                               @RequestParam(required = false, defaultValue = "10") int size) {
        return privateEventService.getAllEvents(userId, getPageRequest(from, size));
    }

    @PostMapping
    public ResponseEntity<Object> createNewEvent(@PathVariable long userId,
                                                 @Valid @RequestBody NewEventDto eventRequest) {
        LocalDateTime eventDate = LocalDateTime.parse(eventRequest.getEventDate(), TIMESTAMP_FORMATTER);
        if (LocalDateTime.now().isAfter(eventDate)) {
            throw new ForbiddenEventDateException(String.format("EventDate: '%s' must not be in past",
                    eventRequest.getEventDate()));
        }
        return privateEventService.createNewEvent(userId, eventRequest);
    }


}

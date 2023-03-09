package ru.practicum.main.service.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.NewEventDto;
import ru.practicum.main.service.service.PrivateEventService;

import javax.validation.Valid;

@RestController
@RequestMapping("/users/{userId}/events")
@RequiredArgsConstructor
public class PrivateEventController {
    private final PrivateEventService privateEventService;

    @PostMapping
    public ResponseEntity<Object> createNewEvent(@PathVariable long userId,
                                                 @Valid @RequestBody NewEventDto eventRequest) {
        return privateEventService.createNewEvent(userId, eventRequest);
    }
}

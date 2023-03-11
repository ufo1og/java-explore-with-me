package ru.practicum.main.service.controller.priv;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.service.priv.PrivateRequestService;

@RestController
@RequestMapping("/users/{userId}/request")
@RequiredArgsConstructor
public class PrivateRequestController {
    private final PrivateRequestService privateRequestService;

    @PostMapping
    public ResponseEntity<Object> createNewParticipationRequest(@PathVariable long userId, @RequestParam long eventId) {
        return privateRequestService.createNewParticipationRequest(userId, eventId);
    }
}

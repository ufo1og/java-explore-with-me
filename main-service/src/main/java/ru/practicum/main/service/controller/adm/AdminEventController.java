package ru.practicum.main.service.controller.adm;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.UpdateEventAdminRequest;
import ru.practicum.main.service.service.adm.AdminEventService;
import ru.practicum.main.service.utils.EventState;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.stats.dto.ConstantValues.TIMESTAMP_FORMATTER;
import static ru.practicum.main.service.utils.PageRequestGetter.getPageRequest;

@RestController
@RequestMapping("/admin/events")
@RequiredArgsConstructor
public class AdminEventController {
    private final AdminEventService adminEventService;

    @GetMapping
    public ResponseEntity<Object> getEvents(@RequestParam List<Long> users,
                                            @RequestParam List<EventState> states,
                                            @RequestParam List<Long> categories,
                                            @RequestParam String rangeStart,
                                            @RequestParam String rangeEnd,
                                            @RequestParam(required = false, defaultValue = "0") int from,
                                            @RequestParam(required = false, defaultValue = "10") int size) {
        if (users == null) {
            throw new IllegalArgumentException("Users must not be null");
        }
        if (states == null) {
            throw new IllegalArgumentException("States must not be null");
        }
        if (categories == null) {
            throw new IllegalArgumentException("Categories must not be null");
        }
        if (rangeStart == null || rangeEnd == null) {
            throw new IllegalArgumentException("RangeStart and RangeEnd must not be null");
        }
        LocalDateTime start = LocalDateTime.parse(rangeStart, TIMESTAMP_FORMATTER);
        LocalDateTime end = LocalDateTime.parse(rangeEnd, TIMESTAMP_FORMATTER);
        return adminEventService.findEvents(users, states, categories, start, end, getPageRequest(from, size));
    }

    @PatchMapping("/{eventId}")
    public ResponseEntity<Object> updateEvent(@PathVariable long eventId,
                                              @RequestBody UpdateEventAdminRequest request) {
        return adminEventService.updateEvent(eventId, request);
    }
}

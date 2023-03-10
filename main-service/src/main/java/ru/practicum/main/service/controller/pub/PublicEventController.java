package ru.practicum.main.service.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.main.service.dto.EventFullDto;
import ru.practicum.main.service.dto.EventShortDto;
import ru.practicum.main.service.service.pub.PublicEventService;
import ru.practicum.main.service.utils.EventSort;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.EndpointHitDto;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.main.service.utils.PageRequestGetter.getPageRequest;
import static ru.practicum.stats.dto.ConstantValues.TIMESTAMP_FORMATTER;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {
    private final StatsClient statsClient;
    private final PublicEventService publicEventService;
    private final String APP_NAME = "emw-main-service";

    @GetMapping
    public ResponseEntity<Object> getAllEvents(
            @RequestParam String text,
            @RequestParam long[] categories,
            @RequestParam boolean paid,
            @RequestParam(required = false) String rangeStart,
            @RequestParam(required = false) String rangeEnd,
            @RequestParam(required = false, defaultValue = "false") boolean onlyAvailable,
            @RequestParam String sort,
            @RequestParam int from,
            @RequestParam int size,
            HttpServletRequest request) {
        if (text.isBlank()) {
            throw new IllegalArgumentException("Parameter 'text' must not be blank");
        }
        EventSort eventSort = EventSort.valueOf(sort);
        List<EventShortDto> result;
        if(rangeStart == null || rangeEnd == null) {
            result = publicEventService.getAllFutureEvents(text, categories, paid, onlyAvailable, eventSort,
                    getPageRequest(from, size));
        } else {
            LocalDateTime start = LocalDateTime.parse(rangeStart, TIMESTAMP_FORMATTER);
            LocalDateTime end = LocalDateTime.parse(rangeEnd, TIMESTAMP_FORMATTER);
            result = publicEventService.getAllEvents(text, categories, paid, start, end, onlyAvailable, eventSort,
                    getPageRequest(from, size));
        }
        if (!result.isEmpty()) {
            String uri = request.getRequestURI();
            String ipAddress = request.getRemoteAddr();
            String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
            for (EventShortDto event : result) {
                EndpointHitDto hit = new EndpointHitDto(APP_NAME, String.format("%s/%s", uri, event.getId()),
                        ipAddress, timestamp);
                statsClient.hitEvent(hit);
            }
        }
        return new ResponseEntity<>(result, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<Object> getEvent(@PathVariable long id, HttpServletRequest request) {
        EventFullDto event = publicEventService.getEvent(id);
        String uri = request.getRequestURI();
        String ipAddress = request.getRemoteAddr();
        String timestamp = LocalDateTime.now().format(TIMESTAMP_FORMATTER);
        EndpointHitDto hit = new EndpointHitDto(APP_NAME, uri, ipAddress, timestamp);
        statsClient.hitEvent(hit);
        return new ResponseEntity<>(event, HttpStatus.OK);
    }
}

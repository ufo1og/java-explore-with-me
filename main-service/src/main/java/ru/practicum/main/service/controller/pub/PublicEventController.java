package ru.practicum.main.service.controller.pub;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.main.service.service.pub.PublicEventService;
import ru.practicum.main.service.utils.EventSort;
import ru.practicum.stats.client.StatsClient;

import javax.servlet.http.HttpServletRequest;

import java.time.LocalDateTime;

import static ru.practicum.main.service.utils.PageRequestGetter.getPageRequest;
import static ru.practicum.stats.dto.ConstantValues.TIMESTAMP_FORMATTER;

@RestController
@RequestMapping("/events")
@RequiredArgsConstructor
public class PublicEventController {
    @Value("${stats.server.url}")
    private String statsServerUrl;
    private StatsClient statsClient = new StatsClient(statsServerUrl);
    private final PublicEventService publicEventService;

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
        ResponseEntity<Object> result;
        if(rangeStart == null || rangeEnd == null) {
            result = publicEventService.getAllFutureEvents(text, categories, paid, onlyAvailable, sort,
                    getPageRequest(from, size));
        } else {
            LocalDateTime start = LocalDateTime.parse(rangeStart, TIMESTAMP_FORMATTER);
            LocalDateTime end = LocalDateTime.parse(rangeEnd, TIMESTAMP_FORMATTER);
            result = publicEventService.getAllEvents(text, categories, paid, start, end, onlyAvailable, sort,
                    getPageRequest(from, size));
        }
        statsClient.hitEvent() //TODO остановился здесь!
        return result;
    }
}

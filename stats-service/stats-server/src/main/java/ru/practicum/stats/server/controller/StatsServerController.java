package ru.practicum.stats.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import ru.practicum.stats.dto.ViewStats;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.server.service.StatsServerService;

import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.stats.server.utils.StatsServerConstantValues.TIMESTAMP_FORMATTER;

@RestController
@RequiredArgsConstructor
public class StatsServerController {
    private final StatsServerService service;

    @PostMapping("/hit")
    public ResponseEntity save(@RequestBody EndpointHitDto requestDto) {
        return service.saveEndpointRequest(requestDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<List<ViewStats>> find(@RequestParam String start, @RequestParam String end,
                                                @RequestParam String[] uris,
                                                @RequestParam(defaultValue = "false") boolean unique) {
        LocalDateTime startDate = LocalDateTime.parse(start, TIMESTAMP_FORMATTER);
        LocalDateTime endDate = LocalDateTime.parse(end, TIMESTAMP_FORMATTER);
        return service.findEndpointRequests(startDate, endDate, uris, unique);
    }
}

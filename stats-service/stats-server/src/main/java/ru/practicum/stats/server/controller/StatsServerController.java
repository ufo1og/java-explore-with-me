package ru.practicum.stats.server.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.yaml.snakeyaml.util.UriEncoder;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.server.service.StatsServerService;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import static ru.practicum.stats.dto.ConstantValues.TIMESTAMP_FORMATTER;

@RestController
@RequiredArgsConstructor
public class StatsServerController {
    private final StatsServerService service;

    @PostMapping("/hit")
    public ResponseEntity<Object> save(@RequestBody EndpointHitDto requestDto) {
        return service.saveEndpointRequest(requestDto);
    }

    @GetMapping("/stats")
    public ResponseEntity<Object> find(@RequestParam String start, @RequestParam String end,
                                                @RequestParam String[] uris,
                                                @RequestParam(defaultValue = "false") boolean unique) {
        String decodedStart = UriEncoder.decode(start);
        String decodedEnd = UriEncoder.decode(end);
        List<String> decodedUris = new ArrayList<>();
        for (String uri : uris) {
            decodedUris.add(UriEncoder.decode(uri));
        }
        LocalDateTime startDate = LocalDateTime.parse(decodedStart, TIMESTAMP_FORMATTER);
        LocalDateTime endDate = LocalDateTime.parse(decodedEnd, TIMESTAMP_FORMATTER);
        return service.findEndpointRequests(startDate, endDate, decodedUris, unique);
    }
}

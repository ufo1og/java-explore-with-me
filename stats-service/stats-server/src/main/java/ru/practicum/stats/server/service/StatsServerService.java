package ru.practicum.stats.server.service;

import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.stats.dto.ViewStats;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.server.dto.EndpointRequestConverter;
import ru.practicum.stats.server.model.EndpointHit;
import ru.practicum.stats.server.repository.StatsServerRepository;

import java.time.LocalDateTime;
import java.util.List;

@Service
@RequiredArgsConstructor
public class StatsServerService {
    private final StatsServerRepository repository;


    public ResponseEntity saveEndpointRequest(EndpointHitDto requestDto) {
        EndpointHit endpointHit = EndpointRequestConverter.fromDto(requestDto);
        repository.save(endpointHit);
        return new ResponseEntity(HttpStatus.CREATED);
    }

    public ResponseEntity<List<ViewStats>> findEndpointRequests(LocalDateTime startDate, LocalDateTime endDate,
                                                                String[] uris, boolean unique) {
        List<ViewStats> hits;
        if (unique) {
            hits = repository.countUniqueHits(startDate, endDate, uris);
        } else {
            hits = repository.countHits(startDate, endDate, uris);
        }
        return new ResponseEntity<>(hits, HttpStatus.OK);
    }
}

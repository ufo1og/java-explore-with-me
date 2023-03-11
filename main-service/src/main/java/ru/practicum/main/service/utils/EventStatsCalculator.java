package ru.practicum.main.service.utils;

import ru.practicum.main.service.model.Event;
import ru.practicum.main.service.repository.ParticipationRequestRepository;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.ViewStats;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class EventStatsCalculator {
    public static Map<Long, Long> getEventViews(List<Event> events, StatsClient statsClient) {
        Map<Long, Long> result = events.stream().collect(Collectors.toMap(Event::getId, event -> 0L));
        Optional<LocalDateTime> earliestPublishedDate = events.stream()
                .map(Event::getPublishedOn)
                .filter(Objects::nonNull)
                .min(LocalDateTime::compareTo);
        if (earliestPublishedDate.isPresent()) {
            List<String> uris = events.stream()
                    .map(event -> String.format("/events/%s", event.getId()))
                    .collect(Collectors.toList());
            LocalDateTime startRequestDate = earliestPublishedDate.get();
            LocalDateTime endRequestDate = LocalDateTime.now();
            List<ViewStats> viewStats = statsClient.getHits(startRequestDate, endRequestDate, uris, true);
            for (ViewStats s : viewStats) {
                String uri = s.getUri();
                Long eventId = Long.parseLong(uri.substring(uri.lastIndexOf("/") + 1));
                result.put(eventId, s.getHits());
            }
        }
        return result;
    }

    public static Map<Long, Integer> getEventConfirmedRequests(List<Event> events,
                                                               ParticipationRequestRepository repository) {
        Map<Long, Integer> result = events.stream().collect(Collectors.toMap(Event::getId, event -> 0));
        List<EventAcceptedParticipations> acceptedRequests = repository.getConfirmedRequestsCount(result.keySet());
        for (EventAcceptedParticipations eap : acceptedRequests) {
            result.put(eap.getEventId(), eap.getAcceptedRequests().intValue());
        }
        return result;
    }
}

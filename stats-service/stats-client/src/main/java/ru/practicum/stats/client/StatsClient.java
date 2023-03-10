package ru.practicum.stats.client;

import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.reactive.function.client.WebClient;
import reactor.core.publisher.Mono;
import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.dto.ViewStats;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.List;

import static ru.practicum.stats.dto.ConstantValues.TIMESTAMP_FORMATTER;

public class StatsClient {
    private final WebClient client;

    public StatsClient(String serverUrl) {
        this.client = WebClient.create(serverUrl);
    }

    public ResponseEntity<Object> hitEvent(EndpointHitDto endpointHitDto) {
        return client
                .post()
                .uri("/hit")
                .body(Mono.just(endpointHitDto), EndpointHitDto.class)
                .retrieve()
                .toEntity(Object.class)
                .block();
    }

    public List<ViewStats> getHits(LocalDateTime start, LocalDateTime end, List<String> uris, boolean unique) {
        StringBuilder requestBuilder = new StringBuilder("/stats?")
                .append("start=").append(URLEncoder.encode(start.format(TIMESTAMP_FORMATTER), StandardCharsets.UTF_8))
                .append("&end=").append(URLEncoder.encode(end.format(TIMESTAMP_FORMATTER), StandardCharsets.UTF_8));
        for (String uri : uris) {
            requestBuilder.append("&uris=")
                    .append(URLEncoder.encode(uri, StandardCharsets.UTF_8));
        }
        requestBuilder.append("&unique=").append(unique);

        return client
                .get()
                .uri(requestBuilder.toString())
                .accept(MediaType.APPLICATION_JSON)
                .acceptCharset(StandardCharsets.UTF_8)
                .retrieve()
                .bodyToMono(new ParameterizedTypeReference<List<ViewStats>>() {})
                .block();
    }
}

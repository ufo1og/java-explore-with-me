package ru.practicum.stats.server.dto;

import ru.practicum.stats.dto.EndpointHitDto;
import ru.practicum.stats.server.model.EndpointHit;

import java.time.LocalDateTime;

import static ru.practicum.stats.dto.ConstantValues.TIMESTAMP_FORMATTER;

public class EndpointRequestConverter {
    public static EndpointHit fromDto(EndpointHitDto requestDto) {
        return new EndpointHit(
                null,
                requestDto.getApp(),
                requestDto.getUri(),
                requestDto.getIp(),
                LocalDateTime.parse(requestDto.getTimestamp(), TIMESTAMP_FORMATTER)
        );
    }
}

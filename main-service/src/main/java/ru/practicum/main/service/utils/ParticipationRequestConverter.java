package ru.practicum.main.service.utils;

import ru.practicum.main.service.dto.ParticipationRequestDto;
import ru.practicum.main.service.model.ParticipationRequest;

import static ru.practicum.stats.dto.ConstantValues.TIMESTAMP_FORMATTER;

public class ParticipationRequestConverter {
    public static ParticipationRequestDto participationRequestDto(ParticipationRequest participationRequest) {
        return new ParticipationRequestDto(
                participationRequest.getCreated().format(TIMESTAMP_FORMATTER),
                participationRequest.getEvent().getId(),
                participationRequest.getId(),
                participationRequest.getRequester().getId(),
                participationRequest.getStatus().toString()
        );
    }
}

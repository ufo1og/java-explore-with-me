package ru.practicum.main.service.utils;

import ru.practicum.main.service.dto.ParticipationRequestDto;
import ru.practicum.main.service.model.ParticipationRequest;

import java.util.List;
import java.util.stream.Collectors;

import static ru.practicum.stats.dto.ConstantValues.TIMESTAMP_FORMATTER;

public class ParticipationRequestConverter {
    public static ParticipationRequestDto toParticipationRequestDto(ParticipationRequest participationRequest) {
        return new ParticipationRequestDto(
                participationRequest.getCreated().format(TIMESTAMP_FORMATTER),
                participationRequest.getEvent().getId(),
                participationRequest.getId(),
                participationRequest.getRequester().getId(),
                participationRequest.getStatus().toString()
        );
    }

    public static List<ParticipationRequestDto> toParticipationRequestDto(
            List<ParticipationRequest> participationRequests) {
        return participationRequests.stream()
                .map(ParticipationRequestConverter::toParticipationRequestDto)
                .collect(Collectors.toList());
    }
}

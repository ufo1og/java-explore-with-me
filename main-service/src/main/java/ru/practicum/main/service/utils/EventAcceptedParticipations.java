package ru.practicum.main.service.utils;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@RequiredArgsConstructor
public class EventAcceptedParticipations {
    private Long eventId;
    private Long acceptedRequests;

}
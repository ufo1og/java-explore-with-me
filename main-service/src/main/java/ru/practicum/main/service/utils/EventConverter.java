package ru.practicum.main.service.utils;

import ru.practicum.main.service.dto.*;
import ru.practicum.main.service.model.Category;
import ru.practicum.main.service.model.Event;
import ru.practicum.main.service.model.Location;
import ru.practicum.main.service.model.User;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

import static ru.practicum.stats.dto.ConstantValues.TIMESTAMP_FORMATTER;

public class EventConverter {
    public static Event toNewEvent(User user, Category category, NewEventDto eventRequest) {
        return new Event(
                null,
                eventRequest.getTitle(),
                eventRequest.getAnnotation(),
                eventRequest.getDescription(),
                LocalDateTime.parse(eventRequest.getEventDate(), TIMESTAMP_FORMATTER),
                eventRequest.getLocation().getLon(),
                eventRequest.getLocation().getLat(),
                eventRequest.isPaid(),
                eventRequest.getParticipantLimit(),
                eventRequest.isRequestModeration(),
                user,
                category,
                Collections.emptySet(),
                false,
                null,
                EventState.SEND_TO_REVIEW,
                LocalDateTime.now()
        );
    }

    public static EventShortDto toEventShortDto(Event event, Category category, User user) {
        UserShortDto userShortDto = UserConverter.toUserShortDto(user);
        CategoryDto categoryDto = CategoryConverter.toCategoryDto(category);
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                categoryDto,
                0,
                event.getEventDate().format(TIMESTAMP_FORMATTER),
                userShortDto,
                event.getPaid(),
                event.getTitle(),
                0
        );
    }

    public static List<EventShortDto> toEventShortDto(List<Event> events, Map<Long, Long> viewStats,
                                                      Map<Long, Integer> confirmedRequests) {
        List<EventShortDto> result = new ArrayList<>();
        for (Event event : events) {
            Long eventId = event.getId();
            EventShortDto eventShortDto = new EventShortDto(
                    eventId,
                    event.getAnnotation(),
                    CategoryConverter.toCategoryDto(event.getCategory()),
                    confirmedRequests.get(eventId),
                    event.getEventDate().format(TIMESTAMP_FORMATTER),
                    UserConverter.toUserShortDto(event.getInitiator()),
                    event.getPaid(),
                    event.getTitle(),
                    viewStats.get(eventId)
            );
            result.add(eventShortDto);
        }
        return result;
    }

    public static List<EventFullDto> toEventFullDto(List<Event> events, Map<Long, Long> viewStats,
                                                    Map<Long, Integer> confirmedRequests) {
        List<EventFullDto> result = new ArrayList<>();
        for (Event event : events) {
            Long eventId = event.getId();
            EventFullDto eventFullDto = new EventFullDto(
                    eventId,
                    event.getAnnotation(),
                    CategoryConverter.toCategoryDto(event.getCategory()),
                    confirmedRequests.get(eventId),
                    event.getCreatedOn().format(TIMESTAMP_FORMATTER),
                    event.getDescription(),
                    event.getEventDate().format(TIMESTAMP_FORMATTER),
                    UserConverter.toUserShortDto(event.getInitiator()),
                    new Location(event.getLatitude(), event.getLongitude()),
                    event.getPaid(),
                    event.getParticipantLimit(),
                    event.getPublishedOn().format(TIMESTAMP_FORMATTER),
                    event.getRequestModeration(),
                    event.getState().toString(),
                    event.getTitle(),
                    viewStats.get(eventId)
            );
            result.add(eventFullDto);
        }
        return result;
    }
}

package ru.practicum.main.service.utils;

import ru.practicum.main.service.dto.CategoryDto;
import ru.practicum.main.service.dto.EventShortDto;
import ru.practicum.main.service.dto.NewEventDto;
import ru.practicum.main.service.dto.UserShortDto;
import ru.practicum.main.service.model.Category;
import ru.practicum.main.service.model.Event;
import ru.practicum.main.service.model.User;

import java.time.LocalDateTime;

import static ru.practicum.stats.dto.ConstantValues.TIMESTAMP_FORMATTER;

public class EventConverter {
    public static Event toEvent(User user, Category category, NewEventDto eventRequest) {
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
                category
        );
    }

    public static EventShortDto toEventShortDto(Event event, Category category, User user) {
        UserShortDto userShortDto = UserConverter.toUserShortDto(user);
        CategoryDto categoryDto = CategoryConverter.toCategoryDto(category);
        return new EventShortDto(
                event.getId(),
                event.getAnnotation(),
                categoryDto,
                0, /*TODO*/
                event.getEventDate().format(TIMESTAMP_FORMATTER),
                userShortDto,
                event.getPaid(),
                event.getTitle(),
                0 /*TODO*/
        );
    }
}

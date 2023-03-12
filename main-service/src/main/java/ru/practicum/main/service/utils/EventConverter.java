package ru.practicum.main.service.utils;

import ru.practicum.main.service.dto.*;
import ru.practicum.main.service.exceptions.ForbiddenAccessException;
import ru.practicum.main.service.model.Category;
import ru.practicum.main.service.model.Event;
import ru.practicum.main.service.model.Location;
import ru.practicum.main.service.model.User;
import ru.practicum.main.service.repository.CategoryRepository;
import ru.practicum.main.service.repository.ParticipationRequestRepository;
import ru.practicum.stats.client.StatsClient;
import ru.practicum.stats.dto.ViewStats;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.*;
import java.util.stream.Collectors;

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

    public static List<EventShortDto> toEventShortDto(List<Event> events, StatsClient statsClient,
                                                      ParticipationRequestRepository repository) {
        Map<Long, Long> viewStats = getEventViews(events, statsClient);
        Map<Long, Integer> confirmedRequests = getEventConfirmedRequests(events, repository);
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

    public static List<EventFullDto> toEventFullDto(List<Event> events, StatsClient statsClient,
                                                    ParticipationRequestRepository repository) {
        Map<Long, Long> viewStats = getEventViews(events, statsClient);
        Map<Long, Integer> confirmedRequests = getEventConfirmedRequests(events, repository);
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

    public static Event makeUpdatedEvent(Event updatingEvent, UpdateEventAdminRequest request,
                                         CategoryRepository categoryRepository) {
        if (request.getAnnotation() != null) {
            updatingEvent.setAnnotation(request.getAnnotation());
        }
        if (request.getCategory() != null) {
            long categoryId = request.getCategory();
            Category category = categoryRepository.findById(categoryId).orElseThrow(
                    () -> new EntityNotFoundException(String.format("Category with id=%s not found", categoryId))
            );
            updatingEvent.setCategory(category);
        }
        if (request.getDescription() != null) {
            updatingEvent.setDescription(request.getDescription());
        }
        if (request.getEventDate() != null) {
            LocalDateTime eventDate = LocalDateTime.parse(request.getEventDate(), TIMESTAMP_FORMATTER);
            updatingEvent.setEventDate(eventDate);
        }
        if (request.getLocation() != null) {
            Location location = request.getLocation();
            updatingEvent.setLatitude(location.getLat());
            updatingEvent.setLongitude(location.getLon());
        }
        if (request.getPaid() != null) {
            updatingEvent.setPaid(request.getPaid());
        }
        if (request.getParticipantLimit() != null) {
            updatingEvent.setParticipantLimit(request.getParticipantLimit());
        }
        if (request.getRequestModeration() != null) {
            updatingEvent.setRequestModeration(request.getRequestModeration());
        }
        if (request.getStateAction() != null) {
            EventState state = EventState.valueOf(request.getStateAction());
            if (state.equals(EventState.PUBLISH_EVENT) || state.equals(EventState.REJECT_EVENT)) {
                updatingEvent.setState(state);
            } else {
                throw new ForbiddenAccessException("Admin cant set states except PUBLISH_EVENT and REJECT EVENT");
            }
        }
        if (request.getTitle() != null) {
            updatingEvent.setTitle(request.getTitle());
        }
        return updatingEvent;
    }

     private static Map<Long, Long> getEventViews(List<Event> events, StatsClient statsClient) {
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

    private static Map<Long, Integer> getEventConfirmedRequests(List<Event> events,
                                                               ParticipationRequestRepository repository) {
        Map<Long, Integer> result = events.stream().collect(Collectors.toMap(Event::getId, event -> 0));
        List<EventAcceptedParticipations> acceptedRequests = repository.getConfirmedRequestsCount(result.keySet());
        for (EventAcceptedParticipations eap : acceptedRequests) {
            result.put(eap.getEventId(), eap.getAcceptedRequests().intValue());
        }
        return result;
    }
}

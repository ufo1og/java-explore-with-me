package ru.practicum.main.service.service.priv;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import ru.practicum.main.service.dto.EventRequestStatusUpdateRequest;
import ru.practicum.main.service.dto.ParticipationRequestDto;
import ru.practicum.main.service.exceptions.ForbiddenAccessException;
import ru.practicum.main.service.model.Event;
import ru.practicum.main.service.model.ParticipationRequest;
import ru.practicum.main.service.model.User;
import ru.practicum.main.service.repository.EventRepository;
import ru.practicum.main.service.repository.ParticipationRequestRepository;
import ru.practicum.main.service.repository.UserRepository;
import ru.practicum.main.service.utils.ParticipationRequestConverter;
import ru.practicum.main.service.utils.ParticipationStatus;

import javax.persistence.EntityNotFoundException;
import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PrivateRequestService {
    private final ParticipationRequestRepository participationRequestRepository;
    private final EventRepository eventRepository;
    private final UserRepository userRepository;

    public ResponseEntity<Object> createNewParticipationRequest(long userId, long eventId) {
        if (participationRequestRepository.findByRequesterAndEvent(userId, eventId).isPresent()) {
            throw new ForbiddenAccessException("Cant create more then one Request to Event");
        }
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Event with id=%s not found", eventId))
        );
        User user = userRepository.findById(userId).orElseThrow(
                () -> new EntityNotFoundException(String.format("User with id=%s not found", userId))
        );
        if (user.getId().equals(event.getInitiator().getId())) {
            throw new ForbiddenAccessException("Cant create request for your own Event");
        }
        if (!event.getPublished()) {
            throw new ForbiddenAccessException("Cant create request for unpublished Event");
        }
        if (participationRequestRepository.getConfirmedRequestsCount(eventId) >= event.getParticipantLimit()) {
            throw new ForbiddenAccessException("Cant create request for completely filled Event");
        }
        ParticipationRequest request = new ParticipationRequest(
                null,
                event,
                user,
                event.getRequestModeration() ? ParticipationStatus.PENDING : ParticipationStatus.ACCEPTED,
                LocalDateTime.now()
        );
        request = participationRequestRepository.save(request);
        log.info("Created new ParticipationRequest: {}", request);
        ParticipationRequestDto requestDto = ParticipationRequestConverter.toParticipationRequestDto(request);
        return new ResponseEntity<>(requestDto, HttpStatus.CREATED);
    }

    public ResponseEntity<Object> getEventRequests(long userId, long eventId) {
        List<ParticipationRequest> requests = participationRequestRepository.findUserEventRequests(userId, eventId);
        List<ParticipationRequestDto> requestDtos = ParticipationRequestConverter.toParticipationRequestDto(requests);
        return new ResponseEntity<>(requestDtos, HttpStatus.OK);
    }

    public ResponseEntity<Object> changeRequestsStatus(long userId, long eventId,
                                                       EventRequestStatusUpdateRequest request) {
        Event event = eventRepository.findById(eventId).orElseThrow(
                () -> new EntityNotFoundException(String.format("Event with id=%s not found", eventId))
        );
        List<ParticipationRequest> requests = participationRequestRepository.findUserEventRequests(userId, eventId);

        return null; //TODO
    }
}

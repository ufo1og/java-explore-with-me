package ru.practicum.main.service.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.practicum.main.service.model.ParticipationRequest;
import ru.practicum.main.service.utils.EventAcceptedParticipations;

import java.util.List;
import java.util.Optional;
import java.util.Set;

public interface ParticipationRequestRepository extends JpaRepository<ParticipationRequest, Long> {
    Optional<ParticipationRequest> findByRequesterAndEvent(long requesterId, long eventId);

    @Query("select new ru.practicum.main.service.utils.EventAcceptedParticipations(pr.event.id, count(pr.requester)) " +
            "from ParticipationRequest pr " +
            "where pr.event in ?1 and pr.status = 'ACCEPTED' " +
            "group by pr.event")
    List<EventAcceptedParticipations> getConfirmedRequestsCount(Set<Long> events);

    @Query("select count(pr.requester) " +
            "from ParticipationRequest pr " +
            "where pr.event = ?1 and pr.status = 'ACCEPTED' " +
            "group by pr.event")
    Integer getConfirmedRequestsCount(long eventId);

    @Query("select pr from ParticipationRequest pr " +
            "where pr.event.initiator.id = ?1 and pr.event = ?2")
    List<ParticipationRequest> findUserEventRequests(Long userId, Long eventId);
}

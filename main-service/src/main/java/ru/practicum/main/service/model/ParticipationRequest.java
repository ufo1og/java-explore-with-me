package ru.practicum.main.service.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.main.service.utils.ParticipationStatus;

import javax.persistence.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "PARTICIPATION_REQUESTS")
@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class ParticipationRequest {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;
    @ManyToOne
    @JoinColumn(name = "event_id")
    private Event event;
    @ManyToOne
    @JoinColumn(name = "requester_id")
    private User requester;
    private ParticipationStatus status;
    private LocalDateTime created;
}

package ru.practicum.main.service.model;


import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import ru.practicum.main.service.utils.EventState;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.HashSet;
import java.util.Set;

@Entity
@Table(name = "EVENTS")
@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String title;
    private String annotation;
    private String description;
    private LocalDateTime eventDate;
    private Double latitude;
    private Double longitude;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    @ManyToOne
    @JoinColumn(name = "initiator", nullable = false)
    private User initiator;
    @ManyToOne
    @JoinColumn(name = "category", nullable = false)
    private Category category;
    @ManyToMany(mappedBy = "events")
    private Set<Compilation> compilations = new HashSet<>();
    private Boolean published;
    @Column(name = "published_on")
    private LocalDateTime publishedOn;
    @Enumerated(EnumType.STRING)
    private EventState state;
    @Column(name = "created_on")
    private LocalDateTime createdOn;
}

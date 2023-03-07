package ru.practicum.main.service.model;

import javax.persistence.*;

@Entity
@Table(name = "EVENTS")
public class Event {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}

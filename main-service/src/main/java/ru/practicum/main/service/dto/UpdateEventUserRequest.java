package ru.practicum.main.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import ru.practicum.main.service.model.Location;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class UpdateEventUserRequest {
    @Length(max = 2000, min = 20, message = "Annotation length must be between 20 and 2000")
    private String annotation;
    private Long category;
    @Length(max = 7000, min = 20, message = "Description length must be between 20 and 7000")
    private String description;
    private String eventDate;
    private Location location;
    private Boolean paid;
    private Integer participantLimit;
    private Boolean requestModeration;
    private String stateAction;
    @Length(max = 120, min = 3, message = "Title length must be between 3 and 120")
    private String title;
}

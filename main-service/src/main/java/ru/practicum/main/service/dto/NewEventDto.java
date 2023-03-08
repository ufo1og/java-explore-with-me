package ru.practicum.main.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;
import org.hibernate.validator.constraints.Length;
import ru.practicum.main.service.model.Location;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;

@AllArgsConstructor
@RequiredArgsConstructor
@Getter
@Setter
public class NewEventDto {
    @NotBlank(message = "Annotation must not be blank")
    @Length(max = 2000, min = 20, message = "Annotation length must be between 20 and 2000")
    private String annotation;
    @NotNull(message = "Category field must not be null")
    private Long category;
    @NotBlank(message = "Description must not be blank")
    @Length(max = 7000, min = 20, message = "Description length must be between 20 and 7000")
    private String description;
    @NotBlank(message = "EventDate must not be blank")
    private String eventDate;
    @NotNull(message = "Location field must not be null")
    private Location location;
    private boolean paid = false;
    private int participantLimit = 0;
    private boolean requestModeration = true;
    @NotBlank(message = "Title must not be blank")
    @Length(max = 120, min = 3, message = "Title length must be between 3 and 120")
    private String title;
}

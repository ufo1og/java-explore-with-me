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
public class UpdateEventUserRequest extends UpdateEventAdminRequest {
}

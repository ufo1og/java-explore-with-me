package ru.practicum.main.service.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.Setter;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotBlank;

import static ru.practicum.main.service.utils.ConstantValues.EMAIL_VALIDATION_REGEXP;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewUserRequest {
    @NotBlank(message = "Email is empty")
    @Email(message = "Email is not valid", regexp = EMAIL_VALIDATION_REGEXP)
    private String email;
    @NotBlank(message = "Name is empty")
    private String name;
}
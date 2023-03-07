package ru.practicum.main.service.dto;

import lombok.*;

import javax.validation.constraints.NotBlank;

@RequiredArgsConstructor
@AllArgsConstructor
@Getter
@Setter
public class NewCategoryDto {
    @NotBlank
    private String name;
}

package ru.practicum.main.service.utils;

import ru.practicum.main.service.dto.NewUserRequest;
import ru.practicum.main.service.dto.UserDto;
import ru.practicum.main.service.dto.UserShortDto;
import ru.practicum.main.service.model.User;

public class UserConverter {
    public static User toUser(NewUserRequest userRequest) {
        return new User(null, userRequest.getEmail(), userRequest.getName());
    }

    public static UserDto toUserDto(User user) {
        return new UserDto(user.getId(), user.getEmail(), user.getName());
    }

    public static UserShortDto toUserShortDto(User user) {
        return new UserShortDto(user.getId(), user.getName());
    }
}

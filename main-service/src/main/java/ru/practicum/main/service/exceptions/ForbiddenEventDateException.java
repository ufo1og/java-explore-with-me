package ru.practicum.main.service.exceptions;

public class ForbiddenEventDateException extends ForbiddenAccessException {
    public ForbiddenEventDateException(String message) {
        super(message);
    }
}

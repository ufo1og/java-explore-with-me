package ru.practicum.stats.dto;

import java.time.format.DateTimeFormatter;

public abstract class ConstantValues {
    public static final String TIMESTAMP_FORMAT_PATTERN = "yyyy-MM-dd HH:mm:ss";
    public static final DateTimeFormatter TIMESTAMP_FORMATTER = DateTimeFormatter.ofPattern(TIMESTAMP_FORMAT_PATTERN);
}
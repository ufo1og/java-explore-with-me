package ru.practicum.main.service.utils;

import org.springframework.data.domain.PageRequest;

public class PageRequestGetter {
    public static PageRequest getPageRequest(int from, int size) {
        if (from < 0) {
            throw new IllegalArgumentException("Parameter 'from' can't be negative");
        }
        if (size <= 0) {
            throw new IllegalArgumentException("Parameter 'size' can't be negative or zero");
        }
        return PageRequest.of(from, size);
    }
}

package ru.practicum.main.service.service.pub;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import ru.practicum.main.service.dto.EventFullDto;
import ru.practicum.main.service.dto.EventShortDto;
import ru.practicum.main.service.repository.EventRepository;
import ru.practicum.main.service.utils.EventSort;
import ru.practicum.stats.client.StatsClient;

import java.time.LocalDateTime;
import java.util.List;

@Service
@Slf4j
@RequiredArgsConstructor
public class PublicEventService {
    private final EventRepository eventRepository;
    private final StatsClient statsClient;


    public List<EventShortDto> getAllEvents(String text, long[] categories, boolean paid, LocalDateTime start,
                                            LocalDateTime end, boolean onlyAvailable, EventSort sort,
                                            PageRequest pageRequest) {
        return null; //TODO
    }

    public List<EventShortDto> getAllFutureEvents(String text, long[] categories, boolean paid,
                                                     boolean onlyAvailable, EventSort sort, PageRequest pageRequest) {
        return null; //TODO
    }

    public EventFullDto getEvent(long id) {
        return null; //TODO
    }
}

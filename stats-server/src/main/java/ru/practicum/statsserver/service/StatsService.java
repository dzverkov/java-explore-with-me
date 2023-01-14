package ru.practicum.statsserver.service;

import ru.practicum.statsserver.model.EndpointHit;
import ru.practicum.statsserver.model.ViewStats;

import java.time.LocalDateTime;
import java.util.List;

public interface StatsService {
    void addHit(EndpointHit endpointHit);

    List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique);
}

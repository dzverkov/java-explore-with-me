package ru.practicum.statsserver.controller;

import org.mapstruct.Mapper;
import ru.practicum.statsserver.dto.ViewStatsDto;
import ru.practicum.statsserver.model.ViewStats;

@Mapper
public interface StatsMapper {

    ViewStatsDto toViewStatsDto(ViewStats endpointHit);
}

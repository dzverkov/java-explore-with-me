package ru.practicum.statsserver.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.statsserver.dto.ViewStatsDto;
import ru.practicum.statsserver.model.EndpointHit;
import ru.practicum.statsserver.model.ViewStats;
import ru.practicum.statsserver.service.StatsService;

import javax.validation.Valid;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Validated
@RequestMapping
@RequiredArgsConstructor
public class StatsController {

    private final StatsService statsService;

    private final StatsMapper statsMapper = Mappers.getMapper(StatsMapper.class);

    @PostMapping("/hit")
    ResponseEntity<Object> addHit(
            @RequestBody @Valid EndpointHit endpointHit
    ) {

        log.info("Получен запрос POST на сохранении данных о просмотре, параметр: ", endpointHit);
        statsService.addHit(endpointHit);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/stats")
    ResponseEntity<Object> getStats(
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime start,
            @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime end,
            @RequestParam(required = false) List<String> uris,
            @RequestParam(required = false, defaultValue = "false") Boolean unique
    ) {
        log.info("Получен запрос GET на получение статистики.");
        List<ViewStats> statsList = statsService.getStats(start, end, uris, unique);
        List<ViewStatsDto> statsDtoList = statsList.stream().map(statsMapper::toViewStatsDto).collect(Collectors.toList());
        return ResponseEntity.ok(statsDtoList);
    }

}

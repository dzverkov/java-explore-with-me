package ru.practicum.server.locations.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.events.model.EventState;
import ru.practicum.server.locations.dto.EventFullWithLocationDto;
import ru.practicum.server.locations.dto.EventLocationDto;
import ru.practicum.server.locations.dto.LocationTypeDto;
import ru.practicum.server.locations.dto.NewEventLocationDto;
import ru.practicum.server.locations.model.EventFilterParamsWithLocation;
import ru.practicum.server.locations.model.EventFullWithLocation;
import ru.practicum.server.locations.model.EventLocation;
import ru.practicum.server.locations.model.LocationType;
import ru.practicum.server.locations.service.EventLocationService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Validated
@RequestMapping("/admin/location")
@RequiredArgsConstructor
public class EventLocationController {

    private final EventLocationService eventLocationService;

    private final EventLocationMapper eventLocationMapper = Mappers.getMapper(EventLocationMapper.class);

    /////////////////////////////////////
    // Admin: Локации
    // API для работы с локациями
    /////////////////////////////////////

    @GetMapping
    public ResponseEntity<Object> getAllEventLocationAdmin(
            @RequestParam(required = false) List<Long> typeLocations,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size
    ) {
        log.info("Получен запрос GET на получение локаций с типами typeLocationIds: {}", typeLocations);
        List<EventLocation> eventLocationList = eventLocationService.getAllEventLocationAdmin(typeLocations, from, size);
        List<EventLocationDto> eventLocationDtoList = eventLocationList.stream()
                .map(eventLocationMapper::toEventLocationDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventLocationDtoList);
    }

    @GetMapping("/events")
    public ResponseEntity<Object> getEventsByLocationAdmin(
            @RequestParam Long location,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size
    ) {
        EventFilterParamsWithLocation eventFilterParams = new EventFilterParamsWithLocation(location,
                states, categories, rangeStart, rangeEnd);
        log.info("Получен запрос GET на получение всех событий с параметрами: {}.", eventFilterParams);
        List<EventFullWithLocation> eventsList = eventLocationService.getEventsByLocationAdmin(eventFilterParams, from, size);
        List<EventFullWithLocationDto> eventDtoList = eventsList.stream()
                .map(eventLocationMapper::toEventFullWithLocationDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(eventDtoList);
    }

    @GetMapping("/{locationId}")
    public ResponseEntity<Object> getEventLocationAdmin(
            @PathVariable @Positive Long locationId
    ) {
        log.info("Получен запрос GET на получение локации locationId: {}", locationId);
        EventLocation eventLocation = eventLocationService.getEventLocationAdmin(locationId);
        return ResponseEntity.ok(eventLocationMapper.toEventLocationDto(eventLocation));
    }

    @PostMapping
    public ResponseEntity<Object> addEventLocationAdmin(
            @RequestBody @Valid NewEventLocationDto newEventLocationDto
    ) {
        log.info("Получен запрос POST на добавление локации newEventLocationDto: {}", newEventLocationDto);
        EventLocation eventLocation = eventLocationService.addEventLocationAdmin(
                eventLocationMapper.fromNewEventLocationDto(newEventLocationDto));
        return ResponseEntity.ok(eventLocationMapper.toEventLocationDto(eventLocation));
    }

    @DeleteMapping("/{locationId}")
    public ResponseEntity<Object> deleteEventLocationAdmin(
            @PathVariable @Positive Long locationId
    ) {
        log.info("Получен запрос DELETE на удаление локации locationId: {}", locationId);
        eventLocationService.deleteEventLocationAdmin(locationId);
        return ResponseEntity.ok().build();
    }

    /////////////////////////////////////
    // Admin: Типы локаций
    // API для работы с типами локаций
    /////////////////////////////////////

    @GetMapping("/type")
    public ResponseEntity<Object> getAllEventLocationTypeAdmin() {
        log.info("Получен запрос GET на получение всех типов локаций");
        List<LocationType> locationTypeList = eventLocationService.getAllEventLocationTypeAdmin();
        List<LocationTypeDto> locationTypeDtoList = locationTypeList.stream()
                .map(eventLocationMapper::toLocationTypeDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(locationTypeDtoList);
    }

    @GetMapping("/type/{typeId}")
    public ResponseEntity<Object> getEventLocationTypeAdmin(
            @PathVariable @Positive Long typeId
    ) {
        log.info("Получен запрос GET на получение типа локации typeId: {}", typeId);
        LocationType locationType = eventLocationService.getEventLocationTypeAdmin(typeId);
        return ResponseEntity.ok(eventLocationMapper.toLocationTypeDto(locationType));
    }

    @PostMapping("/type")
    public ResponseEntity<Object> addEventLocationTypeAdmin(
            @RequestBody LocationTypeDto newLocationType
    ) {
        log.info("Получен запрос POST на добавление типа локации newLocationType: {}", newLocationType);
        LocationType locationType = eventLocationService.addEventLocationTypeAdmin(
                eventLocationMapper.fromLocationTypeDto(newLocationType));
        return ResponseEntity.ok(eventLocationMapper.toLocationTypeDto(locationType));
    }

    @DeleteMapping("/type/{typeId}")
    public ResponseEntity<Object> deleteEventLocationTypeAdmin(
            @PathVariable @Positive Long typeId
    ) {
        log.info("Получен запрос DELETE на удаление типа локации typeId: {}", typeId);
        eventLocationService.deleteEventLocationTypeAdmin(typeId);
        return ResponseEntity.ok().build();
    }

}

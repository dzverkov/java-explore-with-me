package ru.practicum.server.events.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.events.dto.*;
import ru.practicum.server.events.model.Event;
import ru.practicum.server.events.model.EventFilterParamsAdmin;
import ru.practicum.server.events.model.EventFilterParamsPublic;
import ru.practicum.server.events.model.EventState;
import ru.practicum.server.events.service.EventService;
import ru.practicum.server.request.controller.RequestMapper;
import ru.practicum.server.request.dto.ParticipationRequestDto;
import ru.practicum.server.request.model.Request;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Validated
@RequestMapping
@RequiredArgsConstructor
public class EventController {

    private static final String ADMIN_EVENT_PATH = "/admin/events";

    private static final String PUBLIC_EVENT_PATH = "/events";

    private static final String PRIVATE_EVENT_PATH = "/users";

    private final EventService eventService;

    private final EventMapper eventMapper = Mappers.getMapper(EventMapper.class);

    private final RequestMapper requestMapper = Mappers.getMapper(RequestMapper.class);

    /////////////////////////////////////
    // Public: События
    // Публичный API для работы с событиями
    /////////////////////////////////////

    @GetMapping(PUBLIC_EVENT_PATH)
    ResponseEntity<Object> getAllEventsPublic(
            @RequestParam(required = false) String text,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) Boolean paid,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "false") Boolean onlyAvailable,
            @RequestParam(required = false, defaultValue = "EVENT_DATE") String sort, // Available values : EVENT_DATE, VIEWS
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size,
            HttpServletRequest request
    ) {
        EventFilterParamsPublic eventFilterParams = new EventFilterParamsPublic(text, categories, paid, rangeStart, rangeEnd,
                onlyAvailable, sort);
        log.info("Получен запрос GET на получение всех событий с параметрами: {}.", eventFilterParams);

        List<Event> eventsList = eventService.getAllEventsPublic(eventFilterParams, from, size, request);
        List<EventShortDto> eventShortDtoList = eventsList.stream().map(eventMapper::toEventShortDto).collect(Collectors.toList());
        return ResponseEntity.ok(eventShortDtoList);
    }

    @GetMapping(PUBLIC_EVENT_PATH + "/{id}")
    ResponseEntity<Object> getEventByIdPublic(
            @PathVariable @Positive Long id,
            HttpServletRequest request
    ) {
        log.info("Получен запрос GET на получение события с Id: {}.", id);

        EventFullDto eventFullDto = eventMapper.toEventFullDto(eventService.getEventByIdPublic(id, request));
        return ResponseEntity.ok(eventFullDto);
    }

    /////////////////////////////////////
    // Private: События
    // Закрытый API для работы с событиями
    /////////////////////////////////////

    @GetMapping(PRIVATE_EVENT_PATH + "/{userId}/events")
    ResponseEntity<Object> getAllEventsByUserPrivate(
            @PathVariable @Positive Long userId,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size
    ) {
        log.info("Получен запрос GET на получение события пользователя с userId: {}.", userId);
        List<Event> eventsList = eventService.getAllEventsByUserPrivate(userId, from, size);
        List<EventShortDto> eventShortDtoList = eventsList.stream().map(eventMapper::toEventShortDto).collect(Collectors.toList());
        return ResponseEntity.ok(eventShortDtoList);
    }

    @PatchMapping(PRIVATE_EVENT_PATH + "/{userId}/events")
    ResponseEntity<Object> updateEventByUserPrivate(
            @PathVariable @Positive Long userId,
            @RequestBody @Valid UpdateEventRequestDto updateEventRequestDto
    ) {
        log.info("Получен запрос PATCH на изменение события пользователя с userId: {}.", userId);
        Event event = eventService.updateEventByUserPrivate(userId,
                eventMapper.fromUpdateEventRequestDto(updateEventRequestDto));
        return ResponseEntity.ok(eventMapper.toEventFullDto(event));
    }

    @PostMapping(PRIVATE_EVENT_PATH + "/{userId}/events")
    ResponseEntity<Object> addEventByUserPrivate(
            @PathVariable @Positive Long userId,
            @RequestBody @Valid NewEventDto newEventDto
    ) {
        log.info("Получен запрос POST на добавление события пользователя с userId: {}.", userId);
        Event event = eventService.addEventByUserPrivate(userId, eventMapper.fromNewEventDto(newEventDto));
        return ResponseEntity.ok(eventMapper.toEventFullDto(event));
    }

    @GetMapping(PRIVATE_EVENT_PATH + "/{userId}/events/{eventId}")
    ResponseEntity<Object> getEventByUserIdAndEventIdPrivate(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId
    ) {
        log.info("Получен запрос GET на получение полной информации о события с eventId: {} " +
                "от пользователя с userId: {}.", eventId, userId);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(eventService.getEventByUserIdAndEventIdPrivate(userId, eventId));
        return ResponseEntity.ok(eventFullDto);
    }

    @PatchMapping(PRIVATE_EVENT_PATH + "/{userId}/events/{eventId}")
    ResponseEntity<Object> cancellationEventByUserIdAndEventIdPrivate(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId
    ) {
        log.info("Получен запрос PATCH на отмену события с eventId: {} " +
                "от пользователя с userId: {}.", eventId, userId);
        EventFullDto eventFullDto = eventMapper.toEventFullDto(
                eventService.cancellationEventByUserIdAndEventIdPrivate(userId, eventId));
        return ResponseEntity.ok(eventFullDto);
    }

    @GetMapping(PRIVATE_EVENT_PATH + "/{userId}/events/{eventId}/requests")
    ResponseEntity<Object> getEventRequestsByUserPrivate(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId
    ) {
        log.info("Получен запрос GET на получение запросов на участие в событии с eventId: {} " +
                "пользователя с userId: {}.", eventId, userId);
        List<Request> requestsList = eventService.getEventRequestsByUserPrivate(userId, eventId);
        List<ParticipationRequestDto> participationRequestDtoList = requestsList.stream()
                .map(requestMapper::toParticipationRequestDto).collect(Collectors.toList());
        return ResponseEntity.ok(participationRequestDtoList);
    }

    @PatchMapping(PRIVATE_EVENT_PATH + "/{userId}/events/{eventId}/requests/{reqId}/confirm")
    ResponseEntity<Object> confirmEventRequestsByUserPrivate(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @PathVariable @Positive Long reqId
    ) {
        log.info("Получен запрос PATCH на подтверждение чужой заявки reqId: {} на участие в событии eventId: {} " +
                "текущего пользователя userId: {}.", reqId, eventId, userId);
        Request request = eventService.confirmEventRequestsByUserPrivate(userId, eventId, reqId);
        return ResponseEntity.ok(requestMapper.toParticipationRequestDto(request));
    }

    @PatchMapping(PRIVATE_EVENT_PATH + "/{userId}/events/{eventId}/requests/{reqId}/reject")
    ResponseEntity<Object> rejectEventRequestsByUserPrivate(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long eventId,
            @PathVariable @Positive Long reqId
    ) {
        log.info("Получен запрос PATCH на отклонение чужой заявки reqId: {} на участие в событии eventId: {} " +
                "текущего пользователя userId: {}.", reqId, eventId, userId);
        Request request = eventService.rejectEventRequestsByUserPrivate(userId, eventId, reqId);
        return ResponseEntity.ok(requestMapper.toParticipationRequestDto(request));
    }

    /////////////////////////////////////
    // Admin: События
    // API для работы с событиями
    /////////////////////////////////////

    @GetMapping(ADMIN_EVENT_PATH)
    ResponseEntity<Object> getAllEventsAdmin(
            @RequestParam(required = false) List<Long> users,
            @RequestParam(required = false) List<EventState> states,
            @RequestParam(required = false) List<Long> categories,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeStart,
            @RequestParam(required = false) @DateTimeFormat(pattern = "yyyy-MM-dd HH:mm:ss") LocalDateTime rangeEnd,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size
    ) {
        EventFilterParamsAdmin eventFilterParams = new EventFilterParamsAdmin(users, states, categories,
                rangeStart, rangeEnd);
        log.info("Получен запрос GET на получение всех событий с параметрами: {}.", eventFilterParams);
        List<Event> eventsList = eventService.getAllEventsAdmin(eventFilterParams, from, size);
        List<EventFullDto> eventFullDtoList = eventsList.stream().map(eventMapper::toEventFullDto).collect(Collectors.toList());
        return ResponseEntity.ok(eventFullDtoList);
    }

    @PutMapping(ADMIN_EVENT_PATH + "/{eventId}")
    ResponseEntity<Object> updateEventAdmin(
            @PathVariable @Positive Long eventId,
            @RequestBody AdminUpdateEventRequestDto adminUpdateEventRequestDto
    ) {
        Event event = eventService.updateEventAdmin(eventId,
                eventMapper.fromAdminUpdateEventRequestDto(adminUpdateEventRequestDto));
        return ResponseEntity.ok(eventMapper.toEventFullDto(event));
    }

    @PatchMapping(ADMIN_EVENT_PATH + "/{eventId}/publish")
    ResponseEntity<Object> publishEventAdmin(
            @PathVariable @Positive Long eventId
    ) {
        Event event = eventService.publishEventAdmin(eventId);
        return ResponseEntity.ok(eventMapper.toEventFullDto(event));
    }

    @PatchMapping(ADMIN_EVENT_PATH + "/{eventId}/reject")
    ResponseEntity<Object> rejectEventAdmin(
            @PathVariable @Positive Long eventId
    ) {
        Event event = eventService.rejectEventAdmin(eventId);
        return ResponseEntity.ok(eventMapper.toEventFullDto(event));
    }
}

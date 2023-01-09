package ru.practicum.server.request.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.request.dto.ParticipationRequestDto;
import ru.practicum.server.request.service.RequestService;

import javax.validation.constraints.Positive;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Validated
@RequestMapping("/users")
@RequiredArgsConstructor
public class RequestController {

    private final RequestService requestService;

    private final RequestMapper requestMapper = Mappers.getMapper(RequestMapper.class);

    /////////////////////////////////////
    // Private: Запросы на участие
    // Закрытый API для работы с запросами текущего пользователя на участие в событиях
    /////////////////////////////////////

    @GetMapping("/{userId}/requests")
    ResponseEntity<Object> getRequestsByUserId(
            @PathVariable @Positive Long userId
    ) {
        log.info("Получен запрос GET на получение запросов пользователя userId: {} на события.", userId);
        List<ParticipationRequestDto> requestsDtoList = requestService.getRequestsByUserId(userId)
                .stream().map(requestMapper::toParticipationRequestDto).collect(Collectors.toList());
        return ResponseEntity.ok(requestsDtoList);
    }

    @PostMapping("/{userId}/requests")
    ResponseEntity<Object> addRequest(
            @PathVariable @Positive Long userId,
            @RequestParam Long eventId
    ) {
        log.info("Получен запрос POST на добавления запроса пользователя userId: {} на событие eventId: {}.",
                userId, eventId);
        ParticipationRequestDto requestDto = requestMapper.toParticipationRequestDto(
                requestService.addRequest(userId, eventId));
        return ResponseEntity.ok(requestDto);
    }

    @PatchMapping("/{userId}/requests/{requestId}/cancel")
    ResponseEntity<Object> cancelRequest(
            @PathVariable @Positive Long userId,
            @PathVariable @Positive Long requestId
    ) {
        log.info("Получен запрос PATCH на отмену запроса requestId: {} пользователя userId: {}.",
                requestId, userId);
        ParticipationRequestDto requestDto = requestMapper.toParticipationRequestDto(
                requestService.cancelRequest(userId, requestId));
        return ResponseEntity.ok(requestDto);
    }

}

package ru.practicum.server.compilation.controller;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.mapstruct.factory.Mappers;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.practicum.server.compilation.dto.CompilationDto;
import ru.practicum.server.compilation.dto.NewCompilationDto;
import ru.practicum.server.compilation.model.Compilation;
import ru.practicum.server.compilation.service.CompilationsService;

import javax.validation.Valid;
import javax.validation.constraints.Positive;
import javax.validation.constraints.PositiveOrZero;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@RestController
@Validated
@RequestMapping
@RequiredArgsConstructor
public class CompilationController {

    private static final String ADMIN_COMPILATIONS_PATH = "/admin/compilations";

    private static final String PUBLIC_COMPILATIONS_PATH = "/compilations";

    private final CompilationsService compilationsService;

    private final CompilationMapper compilationMapper = Mappers.getMapper(CompilationMapper.class);

    /////////////////////////////////////
    // Public: Подборки событий
    // Публичный API для работы с подборками событий
    /////////////////////////////////////

    @GetMapping(PUBLIC_COMPILATIONS_PATH)
    public ResponseEntity<Object> getCompilationsPublic(
            @RequestParam(required = false) Boolean pinned,
            @RequestParam(required = false, defaultValue = "0") @PositiveOrZero Integer from,
            @RequestParam(required = false, defaultValue = "10") @Positive Integer size
    ) {
        log.info("Получен запрос GET на получение подборок событий.");
        List<Compilation> compilationList = compilationsService.getCompilationsPublic(pinned, from, size);
        List<CompilationDto> compilationDtoList = compilationList.stream()
                .map(compilationMapper::toCompilationDto)
                .collect(Collectors.toList());
        return ResponseEntity.ok(compilationDtoList);
    }

    @GetMapping(PUBLIC_COMPILATIONS_PATH + "/{compId}")
    public ResponseEntity<Object> getCompilationByIdPublic(
            @PathVariable @Positive Long compId
    ) {
        log.info("Получен запрос GET на получение подборки событий compId: {}.", compId);
        CompilationDto compilationDto = compilationMapper.toCompilationDto(
                compilationsService.getCompilationByIdPublic(compId));
        return ResponseEntity.ok(compilationDto);
    }

    /////////////////////////////////////
    // Admin: Подборки событий
    // API для работы с подборками событий
    /////////////////////////////////////

    @PostMapping(ADMIN_COMPILATIONS_PATH)
    public ResponseEntity<Object> addCompilationAdmin(
            @RequestBody @Valid NewCompilationDto newCompilationDto
    ) {
        log.info("Получен запрос POST на добавление подборки событий.");
        Compilation compilation = compilationsService.addCompilationAdmin(
                compilationMapper.fromNewCompilationDto(newCompilationDto));
        return ResponseEntity.ok(compilationMapper.toCompilationDto(compilation));
    }

    @DeleteMapping(ADMIN_COMPILATIONS_PATH + "/{compId}")
    public ResponseEntity<Object> deleteCompilationAdmin(
            @PathVariable @Positive Long compId
    ) {
        log.info("Получен запрос DELETE на удаление подборки событий.");
        compilationsService.deleteCompilationAdmin(compId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(ADMIN_COMPILATIONS_PATH + "/{compId}/events/{eventId}")
    public ResponseEntity<Object> deleteEventFromCompilationAdmin(
            @PathVariable @Positive Long compId,
            @PathVariable @Positive Long eventId
    ) {
        log.info("Получен запрос DELETE на удаление события из подборки событий.");
        compilationsService.deleteEventFromCompilationAdmin(compId, eventId);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(ADMIN_COMPILATIONS_PATH + "/{compId}/events/{eventId}")
    public ResponseEntity<Object> addEventToCompilationAdmin(
            @PathVariable @Positive Long compId,
            @PathVariable @Positive Long eventId
    ) {
        log.info("Получен запрос PATCH на добавление события в подборку событий.");
        compilationsService.addEventToCompilationAdmin(compId, eventId);
        return ResponseEntity.ok().build();
    }

    @DeleteMapping(ADMIN_COMPILATIONS_PATH + "/{compId}/pin")
    public ResponseEntity<Object> unpinCompilationAdmin(
            @PathVariable @Positive Long compId
    ) {
        log.info("Получен запрос DELETE на открепление подборки событий.");
        compilationsService.setPinnedCompilationAdmin(compId, false);
        return ResponseEntity.ok().build();
    }

    @PatchMapping(ADMIN_COMPILATIONS_PATH + "/{compId}/pin")
    public ResponseEntity<Object> pinCompilationAdmin(
            @PathVariable @Positive Long compId
    ) {
        log.info("Получен запрос PATCH на закрепление подборки событий.");
        compilationsService.setPinnedCompilationAdmin(compId, true);
        return ResponseEntity.ok().build();
    }

}

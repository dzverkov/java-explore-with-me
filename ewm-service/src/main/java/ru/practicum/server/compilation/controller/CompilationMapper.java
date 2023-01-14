package ru.practicum.server.compilation.controller;


import org.mapstruct.Mapper;
import ru.practicum.server.compilation.dto.CompilationDto;
import ru.practicum.server.compilation.dto.NewCompilationDto;
import ru.practicum.server.compilation.model.Compilation;
import ru.practicum.server.events.model.Event;

import java.util.stream.Collectors;

@Mapper
public interface CompilationMapper {

    CompilationDto toCompilationDto(Compilation compilation);

    default Compilation fromNewCompilationDto(NewCompilationDto newCompilationDto) {
        return new Compilation()
                .toBuilder()
                .events(newCompilationDto.getEvents().stream().map(Event::new).collect(Collectors.toList()))
                .pinned(newCompilationDto.getPinned())
                .title(newCompilationDto.getTitle())
                .build();
    }
}

package ru.practicum.server.request.controller;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.server.request.dto.ParticipationRequestDto;
import ru.practicum.server.request.model.Request;

@Mapper
public interface RequestMapper {

    @Mapping(target = "event", source = "event.id")
    @Mapping(target = "requester", source = "requester.id")
    ParticipationRequestDto toParticipationRequestDto(Request request);
}

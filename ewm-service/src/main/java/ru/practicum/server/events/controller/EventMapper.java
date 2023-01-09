package ru.practicum.server.events.controller;

import org.mapstruct.*;
import ru.practicum.server.events.dto.*;
import ru.practicum.server.events.model.Event;

@Mapper(unmappedTargetPolicy = ReportingPolicy.IGNORE)
public interface EventMapper {

    EventShortDto toEventShortDto(Event event);

    EventFullDto toEventFullDto(Event event);

    @Mapping(target = "category.id", source = "category")
    Event fromNewEventDto(NewEventDto newEventDto);

    @Mapping(target = "category.id", source = "category")
    @Mapping(target = "id", source = "eventId")
    Event fromUpdateEventRequestDto(UpdateEventRequestDto updEventDto);

    @Mapping(target = "category.id", source = "category")
    Event fromAdminUpdateEventRequestDto(AdminUpdateEventRequestDto adminUpdateEventRequestDto);

    Event fromEventFullDto(EventFullDto eventFullDto);

    @BeanMapping(nullValuePropertyMappingStrategy = NullValuePropertyMappingStrategy.IGNORE)
    Event mergeUpdate(Event updateEvent, @MappingTarget Event event);

}

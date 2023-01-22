package ru.practicum.server.locations.controller;

import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import ru.practicum.server.locations.dto.EventFullWithLocationDto;
import ru.practicum.server.locations.dto.EventLocationDto;
import ru.practicum.server.locations.dto.LocationTypeDto;
import ru.practicum.server.locations.dto.NewEventLocationDto;
import ru.practicum.server.locations.model.EventFullWithLocation;
import ru.practicum.server.locations.model.EventLocation;
import ru.practicum.server.locations.model.LocationType;

@Mapper
public interface EventLocationMapper {

    EventLocationDto toEventLocationDto(EventLocation eventLocation);

    @Mapping(target = "locationType.id", source = "locationType")
    EventLocation fromNewEventLocationDto(NewEventLocationDto newEventLocationDto);

    LocationTypeDto toLocationTypeDto(LocationType locationType);

    LocationType fromLocationTypeDto(LocationTypeDto locationTypeDto);

    @Mapping(target = "id", source = "eventId")
    @Mapping(target = "category.id", source = "categoryId")
    @Mapping(target = "category.name", source = "categoryName")
    @Mapping(target = "initiator.id", source = "initiatorId")
    @Mapping(target = "initiator.email", source = "initiatorEmail")
    @Mapping(target = "initiator.name", source = "initiatorName")
    @Mapping(target = "eventLocation.id", source = "eventLocationId")
    @Mapping(target = "eventLocation.name", source = "eventLocationName")
    @Mapping(target = "eventLocation.lat", source = "eventLocationLat")
    @Mapping(target = "eventLocation.lon", source = "eventLocationLon")
    @Mapping(target = "eventLocation.radius", source = "eventLocationRadius")
    @Mapping(target = "eventLocation.locationType.id", source = "eventLocationTypeId")
    @Mapping(target = "eventLocation.locationType.name", source = "eventLocationType")
    EventFullWithLocationDto toEventFullWithLocationDto(EventFullWithLocation eventFullWithLocation);

}

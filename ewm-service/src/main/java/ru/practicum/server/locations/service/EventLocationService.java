package ru.practicum.server.locations.service;

import ru.practicum.server.locations.model.EventFilterParamsWithLocation;
import ru.practicum.server.locations.model.EventFullWithLocation;
import ru.practicum.server.locations.model.EventLocation;
import ru.practicum.server.locations.model.LocationType;

import java.util.List;

public interface EventLocationService {
    List<EventLocation> getAllEventLocationAdmin(List<Long> typeLocationIds, Integer from, Integer size);

    List<EventFullWithLocation> getEventsByLocationAdmin(EventFilterParamsWithLocation eventFilterParams, Integer from, Integer size);

    EventLocation getEventLocationAdmin(Long locationId);

    EventLocation addEventLocationAdmin(EventLocation newEventLocation);

    void deleteEventLocationAdmin(Long locationId);

    List<LocationType> getAllEventLocationTypeAdmin();

    LocationType getEventLocationTypeAdmin(Long typeId);

    LocationType addEventLocationTypeAdmin(LocationType newLocationTypeDto);

    void deleteEventLocationTypeAdmin(Long typeId);
}

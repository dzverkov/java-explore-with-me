package ru.practicum.server.locations.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.practicum.server.exception.ConflictException;
import ru.practicum.server.locations.exception.EventLocationNotFound;
import ru.practicum.server.locations.exception.EventLocationTypeNotFound;
import ru.practicum.server.locations.model.*;
import ru.practicum.server.locations.model.QEventFullWithLocation;
import ru.practicum.server.locations.repository.EventLocationRepository;
import ru.practicum.server.locations.repository.EventLocationTypeRepository;
import ru.practicum.server.locations.repository.EventsByLocationRepository;

import java.util.List;
import java.util.Optional;

@Service
@RequiredArgsConstructor
public class EventLocationServiceImpl implements EventLocationService {

    private final EventLocationRepository eventLocationRepository;

    private final EventLocationTypeRepository eventLocationTypeRepository;

    private final EventsByLocationRepository eventsByLocationRepository;

    @Override
    public List<EventLocation> getAllEventLocationAdmin(List<Long> typeLocationIds, Integer from, Integer size) {
        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);

        if (ObjectUtils.isEmpty(typeLocationIds)) {
            return eventLocationRepository.findAll(pageable).getContent();
        } else {
            return eventLocationRepository.findEventLocationsByLocationType_IdIn(typeLocationIds, pageable).getContent();
        }
    }

    @Override
    public List<EventFullWithLocation> getEventsByLocationAdmin(EventFilterParamsWithLocation eventFilterParams,
                                                                Integer from, Integer size) {

        int page = from / size;
        Pageable pageable = PageRequest.of(page, size);

        QEventFullWithLocation eventFullWithLocation = QEventFullWithLocation.eventFullWithLocation;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(eventFullWithLocation.eventLocationId.eq(eventFilterParams.getLocationId()));

        if (!ObjectUtils.isEmpty(eventFilterParams.getStates())) {
            booleanBuilder.and(eventFullWithLocation.state.in(eventFilterParams.getStates()));
        }

        if (!ObjectUtils.isEmpty(eventFilterParams.getCategories())) {
            booleanBuilder.and(eventFullWithLocation.categoryId.in(eventFilterParams.getCategories()));
        }

        if (eventFilterParams.getRangeStart() != null) {
            booleanBuilder.and(eventFullWithLocation.eventDate.after(eventFilterParams.getRangeStart()));
        }
        if (eventFilterParams.getRangeEnd() != null) {
            booleanBuilder.and(eventFullWithLocation.eventDate.before(eventFilterParams.getRangeEnd()));
        }

        List<EventFullWithLocation> events = eventsByLocationRepository.findAll(booleanBuilder.getValue(), pageable)
                .getContent();

        return events;
    }

    @Override
    public EventLocation getEventLocationAdmin(Long locationId) {

        return eventLocationRepository.findById(locationId)
                .orElseThrow(() -> new EventLocationNotFound(locationId));
    }

    @Override
    public EventLocation addEventLocationAdmin(EventLocation newEventLocation) {

        Optional<EventLocation> eventLocation = eventLocationRepository.findEventLocationByName(
                newEventLocation.getName());
        if (eventLocation.isPresent()) {
            throw new ConflictException("Такая локация уже существует");
        }
        LocationType locationType = eventLocationTypeRepository.findById(newEventLocation.getLocationType().getId())
                .orElseThrow(() -> new EventLocationTypeNotFound(newEventLocation.getLocationType().getId()));
        newEventLocation.setLocationType(locationType);

        return eventLocationRepository.save(newEventLocation);
    }

    @Override
    public void deleteEventLocationAdmin(Long locationId) {
        eventLocationRepository.findById(locationId)
                .orElseThrow(() -> new EventLocationNotFound(locationId));
        eventLocationRepository.deleteById(locationId);
    }

    @Override
    public List<LocationType> getAllEventLocationTypeAdmin() {
        return eventLocationTypeRepository.findAll();
    }

    @Override
    public LocationType getEventLocationTypeAdmin(Long typeId) {
        return eventLocationTypeRepository.findById(typeId)
                .orElseThrow(() -> new EventLocationTypeNotFound(typeId));
    }

    @Override
    public LocationType addEventLocationTypeAdmin(LocationType newLocationTypeDto) {

        Optional<LocationType> eventLocationType = eventLocationTypeRepository.findLocationTypeByName(
                newLocationTypeDto.getName());
        if (eventLocationType.isPresent()) {
            throw new ConflictException("Такой тип локации уже существует");
        }
        return eventLocationTypeRepository.save(newLocationTypeDto);
    }

    @Override
    public void deleteEventLocationTypeAdmin(Long typeId) {
        eventLocationTypeRepository.findById(typeId)
                .orElseThrow(() -> new EventLocationTypeNotFound(typeId));
        eventLocationTypeRepository.deleteById(typeId);
    }
}

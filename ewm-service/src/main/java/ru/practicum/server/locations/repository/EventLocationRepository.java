package ru.practicum.server.locations.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.server.locations.model.EventLocation;

import java.util.List;
import java.util.Optional;

@Repository
public interface EventLocationRepository  extends PagingAndSortingRepository<EventLocation, Long> {

    Page<EventLocation> findEventLocationsByLocationType_IdIn(List<Long> typeLocationIds, Pageable pageable);

    Optional<EventLocation> findEventLocationByName(String name);

}

package ru.practicum.server.locations.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.server.locations.model.LocationType;

import java.util.Optional;

@Repository
public interface EventLocationTypeRepository extends JpaRepository<LocationType, Long> {

    Optional<LocationType> findLocationTypeByName(String name);
}

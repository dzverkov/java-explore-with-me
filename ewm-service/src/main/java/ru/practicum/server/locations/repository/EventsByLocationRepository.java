package ru.practicum.server.locations.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import ru.practicum.server.locations.model.EventFullWithLocation;

public interface EventsByLocationRepository extends PagingAndSortingRepository<EventFullWithLocation, Long>,
        QuerydslPredicateExecutor<EventFullWithLocation> {

}

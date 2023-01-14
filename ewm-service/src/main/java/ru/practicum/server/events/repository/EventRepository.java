package ru.practicum.server.events.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.server.events.model.Event;
import ru.practicum.server.events.model.EventState;

import java.util.List;
import java.util.Optional;


@Repository
public interface EventRepository extends PagingAndSortingRepository<Event, Long>, QuerydslPredicateExecutor<Event> {

    Page<Event> findEventByInitiator_Id(Long userId, Pageable pageable);

    Optional<Event> findEventByIdAndInitiator_Id(Long eventId, Long userId);

    Optional<Event> findEventByIdAndState(Long eventId, EventState state);

    List<Event> getEventsByIdIn(List<Long> eventIds);

}

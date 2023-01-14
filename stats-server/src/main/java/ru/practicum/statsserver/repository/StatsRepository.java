package ru.practicum.statsserver.repository;

import org.springframework.data.querydsl.QuerydslPredicateExecutor;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.stereotype.Repository;
import ru.practicum.statsserver.model.EndpointHit;

@Repository
public interface StatsRepository extends PagingAndSortingRepository<EndpointHit, Long>, QuerydslPredicateExecutor<EndpointHit> {

}

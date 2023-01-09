package ru.practicum.statsserver.service;

import com.querydsl.core.BooleanBuilder;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.util.ObjectUtils;
import ru.practicum.statsserver.model.EndpointHit;
import ru.practicum.statsserver.model.QEndpointHit;
import ru.practicum.statsserver.model.ViewStats;
import ru.practicum.statsserver.repository.StatsRepository;

import java.time.LocalDateTime;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
@RequiredArgsConstructor
public class StatsServiceImpl implements StatsService {


    private final StatsRepository statsRepository;

    @Override
    public void addHit(EndpointHit endpointHit) {
        statsRepository.save(endpointHit);
    }

    @Override
    public List<ViewStats> getStats(LocalDateTime start, LocalDateTime end, List<String> uris, Boolean unique) {

        QEndpointHit endpointHit = QEndpointHit.endpointHit;

        BooleanBuilder booleanBuilder = new BooleanBuilder();

        booleanBuilder.and(endpointHit.timestamp.between(start, end));

        if (!ObjectUtils.isEmpty(uris)) {
            booleanBuilder.and(endpointHit.uri.in(uris));
        }

        List<EndpointHit> hits = (List<EndpointHit>) statsRepository.findAll(booleanBuilder.getValue());

        Stream<ViewStats> viewStatStream = hits.stream()
                .map(hit -> new ViewStats(hit.getApp(), hit.getUri(), hit.getIp(), 1L));
        if (unique) {
            viewStatStream = viewStatStream.distinct();
        }

        List<ViewStats> viewStatList = viewStatStream.collect(
                        Collectors.groupingBy(ViewStats::getStatGroupBy,
                                Collectors.counting())
                ).entrySet().stream()
                .map(e -> new ViewStats(e.getKey().getApp(), e.getKey().getUri(), null, e.getValue()))
                .collect(Collectors.toList());

        return viewStatList;
    }

}

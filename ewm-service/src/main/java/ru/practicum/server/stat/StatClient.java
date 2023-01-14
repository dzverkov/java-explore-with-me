package ru.practicum.server.stat;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.web.client.RestTemplateBuilder;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;
import ru.practicum.server.stat.dto.EndpointHitDto;
import ru.practicum.server.stat.dto.ViewStatsDto;
import ru.practicum.server.utils.EwmDateTimeFormatter;

import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static org.springframework.http.RequestEntity.post;

@RestController
public class StatClient {

    private final RestTemplate restTemplate;
    @Value("${explore-with-me-stats-server.url}")
    private String statServiceUrl;

    private String hitPath = "/hit";
    private String getStatPath = "/stats?start=%s&end=%s&uris=%s&unique=%s";

    private EwmDateTimeFormatter ewmDateTimeFormatter;

    @Autowired
    public StatClient(RestTemplateBuilder builder, EwmDateTimeFormatter ewmDateTimeFormatter) {
        this.restTemplate = builder.build();
        this.ewmDateTimeFormatter = ewmDateTimeFormatter;
    }

    public void addHit(EndpointHitDto endPointHitDto) {
        restTemplate.postForEntity(statServiceUrl + hitPath, endPointHitDto, EndpointHitDto.class);
        post("/hit", endPointHitDto);
    }

    public List<ViewStatsDto> getViewStats(List<Long> eventIds, LocalDateTime start, LocalDateTime end) {

        ViewStatsDto[] viewStats = restTemplate.getForEntity(statServiceUrl + String.format(
                        getStatPath,
                        ewmDateTimeFormatter.toString(start),
                        ewmDateTimeFormatter.toString(end),
                        eventIds.stream().map(evtId -> URLEncoder.encode("/events/" + evtId, StandardCharsets.UTF_8))
                                .collect(Collectors.toList()),
                        "false"),
                ViewStatsDto[].class).getBody();

        return (viewStats != null) ? Arrays.asList(viewStats) : List.of();
    }
}
